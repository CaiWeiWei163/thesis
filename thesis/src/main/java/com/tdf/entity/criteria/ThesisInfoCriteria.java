package com.tdf.entity.criteria;

import com.tdf.util.page.PagedCriteria;

/**
 * @author cww
 * @create 2019-09-12 15:15
 */
public class ThesisInfoCriteria extends PagedCriteria {
    // 作者
    // 关键字
    // 时间区间
    private String author;
    private String guanjianzi;
    private String datesearch;
    private String datesearchstart;
    private String datesearchend;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGuanjianzi() {
        return guanjianzi;
    }

    public void setGuanjianzi(String guanjianzi) {
        this.guanjianzi = guanjianzi;
    }

    public String getDatesearch() {
        return datesearch;
    }

    public void setDatesearch(String datesearch) {
        this.datesearch = datesearch;
    }

    public String getDatesearchstart() {
        return datesearchstart;
    }

    public void setDatesearchstart(String datesearchstart) {
        this.datesearchstart = datesearchstart;
    }

    public String getDatesearchend() {
        return datesearchend;
    }

    public void setDatesearchend(String datesearchend) {
        this.datesearchend = datesearchend;
    }
}
