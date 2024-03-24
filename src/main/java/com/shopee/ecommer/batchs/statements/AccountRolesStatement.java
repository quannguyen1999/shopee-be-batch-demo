package com.shopee.ecommer.batchs.statements;

import com.shopee.ecommer.models.entities.AccountRoles;
import lombok.SneakyThrows;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;

public class AccountRolesStatement implements ItemPreparedStatementSetter<AccountRoles> {

    @SneakyThrows
    @Override
    public void setValues(AccountRoles item, PreparedStatement ps) {
        int i = 1;
        ps.setObject(i++, item.getAccountId());
        ps.setString(i++, item.getRoleId());
    }
}