package com.shopee.ecommer.controllers;

import com.shopee.ecommer.models.request.BatchRequest;
import com.shopee.ecommer.services.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class BatchController {
    @Autowired
    private BatchService batchService;

    @PostMapping("/executeBatch")
    public ResponseEntity<String> sqlToFile(@RequestBody BatchRequest batchRequest) throws Exception {
        batchService.executeBatch(batchRequest);
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
    }
}
