package com.weihuagu.receiptnotice;

import java.util.Map;

public class TestBeanWithPostFullInformationMap {
    private String posturl;
    private Map<String,String> infomap;
    private String pkg;

    public String getPosturl() {
        return posturl;
    }

    public void setPosturl(String posturl) {
        this.posturl = posturl;
    }

    public Map<String, String> getInfomap() {
        return infomap;
    }

    public void setInfomap(Map<String, String> infomap) {
        this.infomap = infomap;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }
}
