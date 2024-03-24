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
public class Category extends CommonBaseEntities {

    private UUID id;

    private String name;

    private String image;


}
