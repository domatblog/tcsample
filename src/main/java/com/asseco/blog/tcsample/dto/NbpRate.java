package com.asseco.blog.tcsample.dto;

import lombok.Data;

import java.util.ArrayList;

@Data
public class NbpRate {
    private String table;
    private String currency;
    private String code;
    private ArrayList<Rate> rates;
    @Data
    public static class Rate{
        private String no;
        private String effectiveDate;
        private double mid;
    }
}
