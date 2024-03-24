package com.shopee.ecommer.models.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

//import javax.xml.bind.annotation.XmlRootElement;

//bắt buộc phải khai báo @XmlRootElement này để tránh in các class lỗi
//@XmlRootElement(name = "error")
@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {
    //tên lỗi
    public String message;

    //lỗi cụ thể (như id)
    public List<String> details;


}
