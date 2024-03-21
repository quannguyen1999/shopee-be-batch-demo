package com.shopee.ecommer.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shopee.ecommer.constants.ConstantValue;
import com.shopee.ecommer.models.request.BatchRequest;
import com.shopee.ecommer.services.BatchService;
import com.shopee.ecommer.utils.FunctionUtils;
import com.shopee.ecommer.validators.BatchValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class BatchImpl implements BatchService {

    @Autowired
    private JobLauncher jobLauncher;

    @Qualifier("sqlToJsonJob")
    @Autowired
    private Job sqlToJsonJob;

    @Qualifier("sqlToCsvJob")
    @Autowired
    private Job sqlToCsvJob;

    @Qualifier("fileToJsonJob")
    @Autowired
    private Job fileToJsonJob;

    @Autowired
    private BatchValidator batchValidator;

    @Override
    public void executeBatchFile(BatchRequest batchRequest) throws JsonProcessingException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        batchValidator.validateBatch(batchRequest);
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("timestamp", String.valueOf(System.currentTimeMillis()))
                .addString("batchRequest", FunctionUtils.ow.writeValueAsString(batchRequest))
                .toJobParameters();
//        if (batchRequest.getTypeFile().equalsIgnoreCase(ConstantValue.CSV)) {
        jobLauncher.run(fileToJsonJob, jobParameters);
//        } else if (batchRequest.getTypeFile().equalsIgnoreCase(ConstantValue.JSON)) {
//            jobLauncher.run(fileToJsonJob, jobParameters);
//        }
    }

    @Override
    public void executeBatchSql(BatchRequest batchRequest) throws JsonProcessingException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        batchValidator.validateBatch(batchRequest);
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("timestamp", String.valueOf(System.currentTimeMillis()))
                .addString("batchRequest", FunctionUtils.ow.writeValueAsString(batchRequest))
                .toJobParameters();
        if (batchRequest.getTypeFile().equalsIgnoreCase(ConstantValue.CSV)) {
            jobLauncher.run(sqlToCsvJob, jobParameters);
        } else if (batchRequest.getTypeFile().equalsIgnoreCase(ConstantValue.JSON)) {
            jobLauncher.run(sqlToJsonJob, jobParameters);
        }
    }
}
