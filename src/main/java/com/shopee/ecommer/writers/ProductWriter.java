package com.shopee.ecommer.writers;

import com.shopee.ecommer.models.Product;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class ProductWriter implements ItemWriter<Product> {


    @Override
    public void write(Chunk<? extends Product> chunk) throws Exception {
        System.out.println("Inside Item Writer");
        chunk.getItems().forEach(System.out::println);
    }

}
