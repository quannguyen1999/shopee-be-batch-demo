package com.shopee.ecommer.models.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class BatchRequest {

    public String table;

    public String typeFile;

}
