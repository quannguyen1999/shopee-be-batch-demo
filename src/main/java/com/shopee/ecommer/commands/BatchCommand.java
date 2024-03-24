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

    @Command(command = "stf", description = "Execute batch Sql to File")
    public ResponseEntity<String> executeSqlToFile(String table, String typeFile) throws Exception {
        batchService.executeBatchSql(BatchRequest.builder()
                .table(table)
                .typeFile(typeFile)
                .build());
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
    }

    @Command(command = "stf-a", description = "Execute All batch File To Sql")
    public ResponseEntity<String> executeFileSqlToFileAll(String typeFile) throws Exception {
        batchService.executeFileSqlToFileAll(typeFile);
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
    }

    @Command(command = "fts", description = "Execute batch File To Sql")
    public ResponseEntity<String> executeFileToSql(String table, String typeFile) throws Exception {
        batchService.executeBatchFile(BatchRequest.builder()
                .table(table)
                .typeFile(typeFile)
                .build());
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
    }

    @Command(command = "fts-a", description = "Execute All batch File To Sql")
    public ResponseEntity<String> executeFileToSqlAll(String typeFile) throws Exception {
        batchService.executeFileToSqlAll(typeFile);
        return ResponseEntity.status(HttpStatus.OK).body(HttpStatus.OK.toString());
    }

}
