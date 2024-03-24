package com.shopee.ecommer.batchs.listeners;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shopee.ecommer.constants.ConstantValue;
import com.shopee.ecommer.models.entities.*;
import com.shopee.ecommer.models.response.ErrorResponse;
import com.shopee.ecommer.utils.FunctionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.annotation.OnSkipInWrite;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.util.Collections;

@Slf4j
@Component
public class CommonListener {

	@OnSkipInWrite
	public void skipInWriter(Object object, Throwable th) throws JsonProcessingException {
		log.debug(th.getMessage());
		if (object instanceof Account) {
			writeFile(Account.class, object, th);
		}
		if (object instanceof AccountRoles) {
			writeFile(AccountRoles.class, object, th);
		}
		if (object instanceof Category) {
			writeFile(Category.class, object, th);
		}
		if (object instanceof Client) {
			writeFile(Client.class, object, th);
		}
		if (object instanceof Product) {
			writeFile(Product.class, object, th);
		}
		if (object instanceof RoleAccount) {
			writeFile(RoleAccount.class, object, th);
		}
	}

	private void writeFile(Class nameClass, Object object, Throwable th) throws JsonProcessingException {
		createFile(ConstantValue.FILE_PATH + "failed/" + nameClass.getSimpleName() + ".json", FunctionUtils.ow.writeValueAsString(ErrorResponse.builder()
				.message(th.getMessage())
				.details(Collections.singletonList(object.toString()))
				.build()));
	}

	private void createFile(String filePath, String data) {
		try (FileWriter fileWriter = new FileWriter(new File(filePath), true)) {
			fileWriter.write(data + ",");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
