package com.shopee.ecommer.models.entities;

import jakarta.persistence.Id;
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
    @Id
    private UUID id;

    private String name;

    private String image;

}
