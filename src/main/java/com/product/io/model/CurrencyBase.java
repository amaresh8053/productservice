package com.product.io.model;

import java.util.Map;

public class CurrencyBase {
    private String base;
    private Map<String,Float> rates;
    private Boolean success;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "CurrencyBase{" +
                "base='" + base + '\'' +
                ", rates=" + rates +
                ", success=" + success +
                '}';
    }
}
