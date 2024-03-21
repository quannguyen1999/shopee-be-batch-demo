package com.shopee.ecommer.commands;

import com.shopee.ecommer.models.request.BatchRequest;
import com.shopee.ecommer.services.BatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.standard.ShellComponent;

@Command
@ShellComponent
public class BatchCommand {

    @Autowired
    private BatchService batchService;

    @Command(command = "eb-s", description = "Execute batch Sql")
    public ResponseEntity<String> executeBatchSql(String table, String typeFile) throws Exception {
        batchService.executeBatchSql(BatchRequest.builder()
                .table(table)
                .typeFile(typeFile)
                .build());
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
    }

    @Command(command = "eb-f", description = "Execute batch File")
    public ResponseEntity<String> executeBatchFile(String table, String typeFile) throws Exception {
        batchService.executeBatchFile(BatchRequest.builder()
                .table(table)
                .typeFile(typeFile)
                .build());
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
    }


}
