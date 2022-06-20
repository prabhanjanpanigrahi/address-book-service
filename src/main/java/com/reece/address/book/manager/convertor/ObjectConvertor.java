package com.reece.address.book.manager.convertor;

import com.reece.address.book.manager.dto.AddressBookDto;
import com.reece.address.book.manager.dto.ContactInfoDto;
import com.reece.address.book.manager.entities.AddressBook;
import com.reece.address.book.manager.entities.ContactInfo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ObjectConvertor {

    ModelMapper modelMapper;

    public AddressBookDto convertToAddressBookDto(AddressBook addressBook) {
        return modelMapper.map(addressBook, AddressBookDto.class);
    }

    public AddressBook convertToAddressBookEntity(AddressBookDto addressBookDto) {
        return modelMapper.map(addressBookDto, AddressBook.class);
    }

    public ContactInfoDto convertToContactInfoDto(ContactInfo contactInfo) {
        return modelMapper.map(contactInfo, ContactInfoDto.class);
    }

    public ContactInfo convertToContactInfoEntity(ContactInfoDto contactInfoDto) {
        return modelMapper.map(contactInfoDto, ContactInfo.class);
    }


    @Bean
    public ModelMapper modelMapper() {
        modelMapper =  new ModelMapper();
        return modelMapper;
    }
}
