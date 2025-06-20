package com.storm.common.dto;

import lombok.Data;

// 分页请求DTO
@Data
public class PageRequest {

    private Integer page = 1;

    private Integer size = 10;

    private String sortBy;

    private String sortDir = "ASC";

    public Integer getOffset() {
        return (page - 1) * size;
    }
}
