package com.smuraha.currency_rates.controller;

import com.smuraha.currency_rates.firebase.entity.RawData;
import com.smuraha.currency_rates.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private TestService testService;

    @PostMapping("/test")
    public String saveTest(@RequestBody RawData test) throws ExecutionException, InterruptedException {
        return testService.saveTest(test);
    }

//    @GetMapping("/test")
//    public List<Test> getTests(@RequestParam("page") int page,@RequestParam("pageSize") int pageSize) throws ExecutionException, InterruptedException {
//        return testService.getTestList(page, pageSize);
//    }
}
