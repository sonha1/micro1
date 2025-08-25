package com.gtel.warehouse.models.response;

import lombok.Data;

import java.util.List;

@Data
public class PagingBaseResponse <T>{

    private int pageSize;
    private int page;
    private int totalPage;
    private long totalRecord;

    private List<T> items;
}
