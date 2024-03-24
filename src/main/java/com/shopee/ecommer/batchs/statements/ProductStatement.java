package com.shopee.ecommer.batchs.statements;

import com.shopee.ecommer.models.entities.Product;
import com.shopee.ecommer.utils.FunctionUtils;
import lombok.SneakyThrows;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;

public class ProductStatement implements ItemPreparedStatementSetter<Product> {

    @SneakyThrows
    @Override
    public void setValues(Product item, PreparedStatement ps) {
        int i = 1;
        ps.setObject(i++, item.getId());
        ps.setString(i++, item.getName());
        ps.setObject(i++, item.getImage());
        ps.setDouble(i++, item.getQuantity());
        ps.setDouble(i++, item.getPrice());
        ps.setDouble(i++, item.getDiscount());
        ps.setString(i++, item.getDescription());
        ps.setDate(i++, new java.sql.Date(FunctionUtils.dateFormat.parse(item.getCreated()).getTime()));
        ps.setDate(i++, new java.sql.Date(FunctionUtils.dateFormat.parse(item.getUpdated()).getTime()));
        ps.setString(i++, item.getUserCreated());
        ps.setString(i++, item.getUserUpdated());
    }
    
}