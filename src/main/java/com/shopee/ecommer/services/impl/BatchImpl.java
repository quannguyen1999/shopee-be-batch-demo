package com.shopee.ecommer.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shopee.ecommer.constants.ConstantValue;
import com.shopee.ecommer.models.request.BatchRequest;
import com.shopee.ecommer.services.BatchService;
import com.shopee.ecommer.utils.BatchUtils;
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

//    @Qualifier("jsonToSqlJob")
//    @Autowired
//    private Job jsonToSqlJob;

    @Qualifier("csvToSqlJob")
    @Autowired
    private Job csvToSqlJob;

    @Autowired
    private BatchValidator batchValidator;

    @Override
    public void executeBatchFile(BatchRequest batchRequest) throws JsonProcessingException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        batchValidator.validateBatch(batchRequest);
        runJob(batchRequest, buildJobParameters(batchRequest), false);
    }

    @Override
    public void executeBatchSql(BatchRequest batchRequest) throws JsonProcessingException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        batchValidator.validateBatch(batchRequest);
        runJob(batchRequest, buildJobParameters(batchRequest), true);
    }

    @Override
    public void executeFileSqlToFileAll(String typeFile) throws JsonProcessingException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        handlerCommonBatch(typeFile, true);
    }

    @Override
    public void executeFileToSqlAll(String typeFile) throws JsonProcessingException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        handlerCommonBatch(typeFile, false);
    }

    private void handlerCommonBatch(String typeFile, Boolean isSqlToFile) {
        BatchRequest batchRequest = BatchRequest.builder()
                .typeFile(typeFile)
                .build();
        batchValidator.validateBatchExecuteAllFile(batchRequest);
        BatchUtils.getListClassesByPackage(ConstantValue.SCAN_PACKAGE_ENTITIES).forEach(val -> {
            try {
                batchRequest.setTable(val.getSimpleName());
                runJob(batchRequest, buildJobParameters(batchRequest), isSqlToFile);
            } catch (JsonProcessingException | JobInstanceAlreadyCompleteException |
                     JobExecutionAlreadyRunningException | JobParametersInvalidException | JobRestartException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void runJob(BatchRequest batchRequest, JobParameters jobParameters, Boolean isSqlToFile) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        if (batchRequest.getTypeFile().equalsIgnoreCase(ConstantValue.CSV)) {
            System.out.println("FUck");
            System.out.println(isSqlToFile);
            System.out.println(isSqlToFile ? "sqlToCsvJob" : "csvToSqlJob");
            jobLauncher.run(isSqlToFile ? sqlToCsvJob : csvToSqlJob, jobParameters);
//            jobLauncher.run(sqlToCsvJob, jobParameters);
        } else if (batchRequest.getTypeFile().equalsIgnoreCase(ConstantValue.JSON)) {
//            System.out.println(isSqlToFile ? "jsonToSqlJob" : "sqlToJsonJo");
//            jobLauncher.run(isSqlToFile ? jsonToSqlJob : sqlToJsonJob, jobParameters);
        }
    }

    private JobParameters buildJobParameters(BatchRequest batchRequest) throws JsonProcessingException {
        return new JobParametersBuilder()
                .addString("timestamp", String.valueOf(System.currentTimeMillis()))
                .addString("batchRequest", FunctionUtils.ow.writeValueAsString(batchRequest)).toJobParameters();

    }
}
