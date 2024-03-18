package com.shopee.ecommer.models;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Product {

    private UUID id;

    private String name;

    private String image;

    private Double quantity;

    private Double price;

    private Double discount;

    private String description;

}
