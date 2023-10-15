package ru.nikitavov.avenir.web.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import ru.nikitavov.avenir.web.security.filter.TokenAuthenticationFilter;
import ru.nikitavov.avenir.web.security.service.auth.JwtAuthenticationProvider;
import ru.nikitavov.avenir.web.security.service.UserDetailsServiceImpl;
import ru.nikitavov.avenir.web.security.service.auth.RestAuthenticationEntryPoint;
import ru.nikitavov.avenir.web.security.service.auth.oauth2.*;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final OAuth2UserServiceImpl oAuth2UserServiceImpl;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final HttpCookieAuthorizationRequestRepository httpCookieAuthorizationRequestRepository;
    private final OAuth2TokenResponseConverter tokenResponseConverter;
    private final JwtAuthenticationProvider authProvider;
    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final RestAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .authenticationProvider(authProvider)
                .userDetailsService(userDetailsServiceImpl)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/swagger-ui/**", "/swagger/**", "/v3/api-docs/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()

                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .and()

                .authorizeRequests()
                .antMatchers("/", "/error", "/favicon.ico", "/**/*.png", "/**/*.gif", "/**/*.svg", "/**/*.jpg",
                        "/**/*.html", "/**/*.css", "/**/*.js")
                .permitAll()
                .antMatchers("/auth/**", "/oauth2/**", "/redirect/**","/point/**", "/schedule/**", "/mail/callback/**", "/bot/**", "/bot/*/callback/**")
                .permitAll().anyRequest().authenticated()
                .and()

                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorize")
                .authorizationRequestRepository(httpCookieAuthorizationRequestRepository)
                .and()

                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")
                .and()

                .userInfoEndpoint()
                .userService(oAuth2UserServiceImpl)
                .and()

                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler);

        // Add our custom Token based authentication filter
        http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(tokenAuthenticationFilter, OAuth2AuthorizationRequestRedirectFilter.class);

        http.oauth2Login().tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient());

        http.authenticationProvider(authProvider);
    }


    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient =
                new DefaultAuthorizationCodeTokenResponseClient();

        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenResponseHttpMessageConverter.setTokenResponseConverter(tokenResponseConverter);

        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
                new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        accessTokenResponseClient.setRestOperations(restTemplate);
        return accessTokenResponseClient;
    }
}
