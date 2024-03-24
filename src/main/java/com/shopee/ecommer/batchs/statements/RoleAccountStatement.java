package com.shopee.ecommer.batchs.statements;

import com.shopee.ecommer.models.entities.RoleAccount;
import lombok.SneakyThrows;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;

public class RoleAccountStatement implements ItemPreparedStatementSetter<RoleAccount> {

    @SneakyThrows
    @Override
    public void setValues(RoleAccount item, PreparedStatement ps) {
        int i = 1;
        ps.setString(i++, item.getCode());
        ps.setString(i++, item.getName());
    }
}