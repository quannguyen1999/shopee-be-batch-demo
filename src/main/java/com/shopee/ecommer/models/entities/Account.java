package com.shopee.ecommer.models.entities;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@FieldNameConstants
public class Account extends CommonBaseEntities {

    private UUID id;

    private String username;

    private String password;

    private String birthday;

    private Boolean gender;

    private String email;

    private String avatar;

    private Boolean isActive;

    private String securityQuestion;

    private String securityAnswer;

    private String mfaSecret;

    private String mfaKeyId;

    private Boolean mfaEnabled;

    private Boolean mfaRegistered;

    private Boolean securityQuestionEnabled;
}
