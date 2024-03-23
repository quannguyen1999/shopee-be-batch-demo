package com.shopee.ecommer.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shopee.ecommer.models.request.BatchRequest;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

public interface BatchService {
    void executeBatchFile(BatchRequest batchRequest) throws JsonProcessingException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException;

    void executeBatchSql(BatchRequest batchRequest) throws JsonProcessingException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException;

    void executeFileSqlToFileAll(String typeFile) throws JsonProcessingException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException;

}
