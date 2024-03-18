package com.shopee.ecommer.controllers;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    private JobLauncher jobLauncher;

    @Qualifier("importFromAccount")
    @Autowired
    private Job accountJob;

    @Qualifier("importFromProduct")
    @Autowired
    private Job productJob;

    @GetMapping("/excel")
    @ResponseBody
    public ResponseEntity<Resource> exportExcel() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("timestamp", String.valueOf(System.currentTimeMillis()))
                .addString("valueTest", "dit con me no work kia moi nguoi")
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(productJob, jobParameters);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/pdf")
    @ResponseBody
    public ResponseEntity<Resource> exportPdf() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("timestamp", String.valueOf(System.currentTimeMillis()))
                .addString("valueTest", "dit con me no work kia moi nguoi")
                .toJobParameters();
        JobExecution jobExecution = jobLauncher.run(accountJob, jobParameters);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
