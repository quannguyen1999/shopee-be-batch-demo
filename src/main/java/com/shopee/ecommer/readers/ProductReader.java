package com.shopee.ecommer.readers;

import com.shopee.ecommer.models.Product;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class ProductReader implements ItemReader<Product> {
    @Override
    public Product read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        return null;
    }
}
