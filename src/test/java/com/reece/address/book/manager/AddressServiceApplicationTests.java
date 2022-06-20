package com.reece.address.book.manager;

import com.reece.address.book.manager.controller.AddressBookController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AddressBookServiceApplication.class)
class AddressServiceApplicationTests {

	@Autowired
	AddressBookController addressBookController;

	@Test
	void contextLoads() {
		assertThat(addressBookController).isNotNull();
	}

}
