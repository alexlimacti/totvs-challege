package com.totvs.challenge.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AccountPageResponse<T> implements PageResponse<T> {

    private List<T> content;
    private int size;
    private long totalSize;
    private int page;
    private int totalPages;

    public void add(T element) {
        content.add(element);
    }
}
