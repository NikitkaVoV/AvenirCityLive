package ru.nikitavov.avenir.web.message.realization.crud.response.region;

public record RegionsResponse(java.util.List<ru.nikitavov.avenir.database.model.entity.Region> list, long totalElements,
                              int totalPages) {
}
