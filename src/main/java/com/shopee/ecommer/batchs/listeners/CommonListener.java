package com.shopee.ecommer.batchs.listeners;

import com.shopee.ecommer.constants.ConstantValue;
import com.shopee.ecommer.models.entities.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;

@Slf4j
@Component
public class CommonListener {

//	@OnSkipInRead
//	public void skipInRead(Throwable th) {
//		System.out.println("test");
////		if(th instanceof FlatFileParseException) {
////			createFile("D:\\M\\U\\Teaching\\Spring-Batch\\Data\\StsProjetcs\\spring-batch\\Chunk Job\\First Chunk Step\\reader\\SkipInRead.txt", 
////					((FlatFileParseException) th).getInput());
////		}
//	}
//
//	@OnSkipInProcess
//	public void skipInProcess(Object studentCsv, Throwable th) {
//		System.out.println("test");
////		createFile("D:\\M\\U\\Teaching\\Spring-Batch\\Data\\StsProjetcs\\spring-batch\\Chunk Job\\First Chunk Step\\processer\\SkipInProcess.txt", 
////				studentCsv.toString());
//	}

	@OnSkipInWrite
	public void skipInWriter(Object object, Throwable th) {
		log.debug(th.getMessage());
		if (object instanceof Product) {
			createFile(ConstantValue.FILE_PATH + "failed/" + Product.class.getSimpleName() + ".txt", object.toString());
		}
	}

	public void createFile(String filePath, String data) {
		try (FileWriter fileWriter = new FileWriter(new File(filePath), true)) {
			fileWriter.write(data + "," + new Date() + "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
