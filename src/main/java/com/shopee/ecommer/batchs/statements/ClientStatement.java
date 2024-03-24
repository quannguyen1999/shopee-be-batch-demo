package com.shopee.ecommer.batchs.statements;

import com.shopee.ecommer.models.entities.Client;
import com.shopee.ecommer.utils.FunctionUtils;
import lombok.SneakyThrows;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;

import java.sql.PreparedStatement;

public class ClientStatement implements ItemPreparedStatementSetter<Client> {

    @SneakyThrows
    @Override
    public void setValues(Client item, PreparedStatement ps) {
        int i = 1;
        ps.setString(i++, item.getId());
        ps.setString(i++, item.getAuthorizationGrantTypes());
        ps.setString(i++, item.getClientAuthenticationMethods());
        ps.setString(i++, item.getClientId());
        ps.setDate(i++, new java.sql.Date(FunctionUtils.dateFormat.parse(item.getClientIdIssuedAt()).getTime()));
        ps.setString(i++, item.getClientName());
        ps.setString(i++, item.getClientSecret());
        ps.setDate(i++, new java.sql.Date(FunctionUtils.dateFormat.parse(item.getClientSecretExpiresAt()).getTime()));
        ps.setString(i++, item.getClientSettings());
        ps.setString(i++, item.getRedirectUris());
        ps.setString(i++, item.getScopes());
        ps.setString(i++, item.getTokenSettings());
    }
}