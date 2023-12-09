package com.ravsdev.ropa.response;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemsResponse {
    private List<?> content = new ArrayList<>();
    private Integer totalPages;
    private Integer currentPage;
    private Long totalItems;

    public ItemsResponse(Page<?> pageContent, List<?> content) {
        this.content = content;
        this.totalItems = pageContent.getTotalElements();
        this.currentPage = pageContent.getNumber();
        this.totalPages = pageContent.getTotalPages();
    }

}
