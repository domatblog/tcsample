package com.asseco.blog.tcsample.service;

import com.asseco.blog.tcsample.dto.NbpRate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="nbpratesclient", path = "/api", url="${spring.cloud.openfeign.client.config.nbpratesclient.url}")
public interface NbpRatesClient {
    @GetMapping(value = "/exchangerates/rates/{nbpTable}/{currency}?format=json")
    NbpRate getRate(@PathVariable String nbpTable, @PathVariable String currency);
}
