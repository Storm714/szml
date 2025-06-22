package com.storm.userservice.common;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// 通用分页响应类
@Data
@NoArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long total;
    private int totalPages;

}
