
package com.codepath.gridimagesearch.models;

import java.io.Serializable;

public class FilterSetting implements Serializable {

    private static final long serialVersionUID = -2948231779179059140L;
    private String imgColor;
    private String imgSize;
    private String imgType;
    private String siteFilter;

    public FilterSetting() {
    }

    public FilterSetting(String imgSize, String imgType, String imgColor, String siteFilter) {
        this.imgSize = imgSize;
        this.imgType = imgType;
        this.imgColor = imgColor;
        this.siteFilter = siteFilter;
    }

    public String getImgColor() {
        return imgColor;
    }

    public void setImgColor(String imgColor) {
        this.imgColor = imgColor;
    }

    public String getImgSize() {
        return imgSize;
    }

    public void setImgSize(String imgSize) {
        this.imgSize = imgSize;
    }

    public String getImgType() {
        return imgType;
    }

    public void setImgType(String imgType) {
        this.imgType = imgType;
    }

    public String getSiteFilter() {
        return siteFilter;
    }

    public void setSiteFilter(String siteFilter) {
        this.siteFilter = siteFilter;
    }

}
