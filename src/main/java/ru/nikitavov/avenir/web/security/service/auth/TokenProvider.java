package ru.nikitavov.avenir.web.security.service.auth;

import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.nikitavov.avenir.database.model.base.User;
import ru.nikitavov.avenir.database.model.security.RefreshToken;
import ru.nikitavov.avenir.database.repository.realisation.RefreshTokenMyRepository;
import ru.nikitavov.avenir.general.model.tuple.Tuple3;
import ru.nikitavov.avenir.general.util.string.HashUtil;
import ru.nikitavov.avenir.web.security.AppProperties;
import ru.nikitavov.avenir.web.security.service.UserService;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

    private final AppProperties appProperties;
    private final RefreshTokenMyRepository refreshTokenRepository;
    private final UserService userService;

    @Transactional
    public Tuple3<String, String, Date> createTokens() {
        User user = userService.getActiveUser();
        return createTokens(user);
    }

    @Transactional
    public Tuple3<String, String, Date> createTokens(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        String accessToken = generateToken(user.getUuid(), expiryDate, appProperties.getAuth().getTokenSecret());
        String refreshToken = createRefreshToken(user, accessToken, expiryDate);
        return new Tuple3<>(accessToken, refreshToken, expiryDate);
    }

    public String getUserUUIDFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(token);

            String hash = HashUtil.sha256(token);
            return refreshTokenRepository.findFirstByAccessTokenHash(hash).isPresent();
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    private String generateToken(Object param, Date expiryDate, String secret) {
        return Jwts.builder()
                .setSubject(param.toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private String createRefreshToken(User user, String accessToken, Date accessExpiryDate) {
        Date now = new Date();
        Date refreshExpiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenRefreshExpirationMsec());

        String refreshToken = generateToken(user.getUuid(), refreshExpiryDate, appProperties.getAuth().getTokenRefreshSecret());

        String accessTokenHash = HashUtil.sha256(accessToken);
        String refreshTokenHash = HashUtil.sha256(refreshToken);

        RefreshToken token = RefreshToken.builder().user(user)
                .accessTokenHash(accessTokenHash).accessExpiryDate(accessExpiryDate)
                .refreshTokenHash(refreshTokenHash).refreshExpiryDate(refreshExpiryDate)
                .build();
        refreshTokenRepository.save(token);

        deleteAllExtraTokens(user.getId());

        return refreshToken;
    }

    private void deleteAllExtraTokens(int userId) {
        List<RefreshToken> userTokens = refreshTokenRepository.getUserTokens(userId);
        if (userTokens.size() <= 10) return;

        List<Integer> ids = userTokens.subList(10, userTokens.size()).stream().map(RefreshToken::getId).toList();
        refreshTokenRepository.deleteAllById(ids);
    }
}
