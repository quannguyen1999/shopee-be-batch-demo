package com.shopee.ecommer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@CommandScan
@SpringBootApplication
public class ShopeeBeBatchDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopeeBeBatchDemoApplication.class, args);
	}

}
