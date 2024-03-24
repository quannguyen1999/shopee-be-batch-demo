package com.shopee.ecommer.models.entities;

import lombok.*;
import lombok.experimental.FieldNameConstants;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldNameConstants
public class Client {

    private String id;

    private String authorizationGrantTypes;

    private String clientAuthenticationMethods;

    private String clientId;

    private String clientIdIssuedAt;

    private String clientName;

    private String clientSecret;

    private String clientSecretExpiresAt;

    private String clientSettings;

    private String redirectUris;

    private String scopes;

    private String tokenSettings;

}
