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
public class RoleAccount extends CommonBaseEntities {

    private String code;

    private String name;

}
