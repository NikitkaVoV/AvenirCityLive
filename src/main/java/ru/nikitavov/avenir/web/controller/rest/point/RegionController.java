package ru.nikitavov.avenir.web.controller.rest.point;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nikitavov.avenir.database.model.entity.Region;
import ru.nikitavov.avenir.database.repository.realisation.RegionRepository;
import ru.nikitavov.avenir.web.message.model.wrapper.MessageWrapper;
import ru.nikitavov.avenir.web.message.realization.crud.response.region.RegionsResponse;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("region")
public class RegionController {

    private final RegionRepository regionRepository;

    @GetMapping("/find")
    public ResponseEntity<MessageWrapper<RegionsResponse>> check(@Param("name") String name, @Param("page") int page, @Param("size") int size) {
        Page<Region> regionPage = regionRepository.findRegions(name, PageRequest.of(page, size, Sort.by("name")));
        return ResponseEntity.ok(new MessageWrapper<>(new RegionsResponse(regionPage.get().toList(), regionPage.getTotalElements(), regionPage.getTotalPages())));
    }


}
