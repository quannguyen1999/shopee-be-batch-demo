package com.shopee.ecommer.batchs.writers;

import lombok.extern.slf4j.Slf4j;
import org.jline.utils.Log;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogItemWriter implements ItemWriter<Object> {

	@Override
	public void write(Chunk items) {
		items.getItems().stream().forEach(Log::info);
	}

}
