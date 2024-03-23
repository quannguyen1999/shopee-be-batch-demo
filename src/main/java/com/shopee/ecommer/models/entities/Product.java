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
public class Product extends CommonBaseEntities {

    private UUID id;

    private String name;

    private String image;

    private Double quantity;

    private Double price;

    private Double discount;

    private String description;


}
