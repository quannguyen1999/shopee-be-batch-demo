package com.shopee.ecommer.batchs.statements;

import com.shopee.ecommer.models.entities.Account;
import com.shopee.ecommer.utils.BatchUtils;
import com.shopee.ecommer.utils.FunctionUtils;
import lombok.SneakyThrows;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;

public class AccountStatement implements ItemPreparedStatementSetter<Account> {
    @SneakyThrows
    @Override
    public void setValues(Account item, PreparedStatement ps) {
        int i = 1;
        ps.setObject(i++, item.getId());
        ps.setString(i++, item.getUsername());
        ps.setObject(i++, item.getPassword());
        ps.setDate(i++, new java.sql.Date(FunctionUtils.dateFormat.parse(item.getBirthday()).getTime()));
        ps.setBoolean(i++, BatchUtils.isBooleanEmpty().apply(item.getGender()));
        ps.setString(i++, item.getEmail());
        ps.setString(i++, item.getAvatar());
        ps.setBoolean(i++, BatchUtils.isBooleanEmpty().apply(item.getIsActive()));
        ps.setString(i++, item.getSecurityQuestion());
        ps.setString(i++, item.getSecurityAnswer());
        ps.setString(i++, item.getMfaSecret());
        ps.setString(i++, item.getMfaKeyId());
        ps.setBoolean(i++, BatchUtils.isBooleanEmpty().apply(item.getMfaEnabled()));
        ps.setBoolean(i++, BatchUtils.isBooleanEmpty().apply(item.getMfaRegistered()));
        ps.setBoolean(i++, BatchUtils.isBooleanEmpty().apply(item.getSecurityQuestionEnabled()));
        ps.setDate(i++, new java.sql.Date(FunctionUtils.dateFormat.parse(item.getCreated()).getTime()));
        ps.setDate(i++, new java.sql.Date(FunctionUtils.dateFormat.parse(item.getUpdated()).getTime()));
        ps.setString(i++, item.getUserCreated());
        ps.setString(i++, item.getUserUpdated());
    }
}