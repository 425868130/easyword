package com.nchu.easyword.dto;

import java.util.List;

/**
 * 2018-3-20 21:24:56
 * @author  xujw
 * 数据分页视图，用于承载返回到前端的分页数据
 * @param <T> 页面内容对象所属类
 */
public class PageViewDTO<T> {
    /*当前页数*/
    int pageIndex;
    /*页面大小*/
    int pageSize;
    /*总记录条数*/
    Long totalRecord;
    /*当前页的数据记录表*/
    List<T> dataList;
    /*数据标题*/
    String title;

    public PageViewDTO() {
    }

    public PageViewDTO(int pageIndex, int pageSize, Long totalRecord, List<T> dataList, String title) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalRecord = totalRecord;
        this.dataList = dataList;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageCount) {
        this.pageSize = pageCount;
    }

    public Long getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(Long totalRecord) {
        this.totalRecord = totalRecord;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
