package com.carlgira.soa.model;

public class AbortResponse {

    private String status;
    private String detail;

    public AbortResponse() {
    }

    public AbortResponse(String status, String detail) {
        this.status = status;
        this.detail = detail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
