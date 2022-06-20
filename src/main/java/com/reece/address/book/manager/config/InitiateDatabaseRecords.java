package com.reece.address.book.manager.config;

import com.reece.address.book.manager.dao.AddressBookRepository;
import com.reece.address.book.manager.entities.AddressBook;
import com.reece.address.book.manager.entities.ContactInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;


@Configuration
@Profile("!test")
@Slf4j
class InitiateDatabaseRecords  {

    @Bean
    CommandLineRunner initDatabase(AddressBookRepository addressBookRepository) {
        if(addressBookRepository.findAll().size()<1) {
            return args -> {
                try {
                    // insert initial records into DB
                    List<ContactInfo> contactInfoList1 = new ArrayList<>();
                    ContactInfo contactInfo1 = new ContactInfo();
                    contactInfo1.setContactNumber("+91-8798797392");
                    contactInfo1.setName("Reece1 Reece1");

                    ContactInfo contactInfo2 = new ContactInfo();
                    contactInfo2.setContactNumber("+91-8873637633");
                    contactInfo2.setName("Reece2 Reece2");
                    contactInfoList1.add(contactInfo1);
                    contactInfoList1.add(contactInfo2);

                    AddressBook addressBook1 = new AddressBook();
                    addressBook1.setName("Test1 Test1");
                    addressBook1.setContactInfoList(contactInfoList1);

                    addressBookRepository.save(addressBook1);

                    List<ContactInfo> contactInfoList2 = new ArrayList<>();
                    ContactInfo contactInfo3 = new ContactInfo();
                    contactInfo3.setContactNumber("+91-8798797392");
                    contactInfo3.setName("Reece1 Reece1");

                    ContactInfo contactInfo4 = new ContactInfo();
                    contactInfo4.setContactNumber("+91-8873637633");
                    contactInfo4.setName("Reece2 Reece2");
                    contactInfoList2.add(contactInfo3);
                    contactInfoList2.add(contactInfo4);
                    AddressBook addressBook2 = new AddressBook();
                    addressBook2.setName("Test2 Test2");
                    addressBook2.setContactInfoList(contactInfoList2);
                    addressBookRepository.save(addressBook2);

                    log.info("Initial records inserted into database." );
                } catch (Exception e) {
                    log.error("Exception occurred while initiating records into database.", e );
                }
            };
        }
        else {
            return args -> {
                log.info("Records are already present in DB.");
            };
        }
    }

}