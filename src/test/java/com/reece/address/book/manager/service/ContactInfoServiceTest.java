package com.reece.address.book.manager.service;

import com.reece.address.book.manager.convertor.ObjectConvertor;
import com.reece.address.book.manager.dao.AddressBookRepository;
import com.reece.address.book.manager.dao.ContactInfoRepository;
import com.reece.address.book.manager.dto.ContactInfoDto;
import com.reece.address.book.manager.entities.AddressBook;
import com.reece.address.book.manager.entities.ContactInfo;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContactInfoServiceTest {

    @Mock
    ContactInfoRepository contactInfoRepository;

    @Mock
    AddressBookRepository addressBookRepository;

    @Mock
    ObjectConvertor objectConvertor;

    @InjectMocks
    ContactInfoService contactInfoService;

    ContactInfo contactInfo;

    ContactInfoDto contactInfoDto;

    @BeforeEach
    public void setUpTestData() {
        contactInfo = new ContactInfo();
        contactInfo.setName("Reece1 Test1");
        contactInfoDto = new ContactInfoDto();
        contactInfoDto.setContactNumber("+91-9878373393");
    }

    @Before("")
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_getAllUniqueContacts() {
        Set<ContactInfo> contactInfoSet = new HashSet<>();
        contactInfoSet.add(contactInfo);
        when(contactInfoRepository.getAllUniqueContacts()).thenReturn(contactInfoSet);
        when(objectConvertor.convertToContactInfoDto(contactInfo)).thenReturn(contactInfoDto);
        List<ContactInfoDto> contactInfoDtoList = contactInfoService.getAllUniqueContacts();
        verify(contactInfoRepository, times(1)).getAllUniqueContacts();
        assertEquals(1, contactInfoDtoList.size());
    }

    @Test
    public void test_createContactInfo() {
        AddressBook addressBook = new AddressBook();
        addressBook.setName("Test1 Test1");
        when(addressBookRepository.findById(1L)).thenReturn(Optional.of(addressBook));
        when(contactInfoRepository.save(contactInfo)).thenReturn(contactInfo);
        when(objectConvertor.convertToContactInfoEntity(contactInfoDto)).thenReturn(contactInfo);
        when(objectConvertor.convertToContactInfoDto(contactInfo)).thenReturn(contactInfoDto);;
        contactInfoDto = contactInfoService.createContactInfo(1L, contactInfoDto);
        verify(contactInfoRepository, times(1)).save(contactInfo);
        assertThat(contactInfoDto).isNotNull();
    }

    @Test
    public void test_deleteContactInfo() {
        when(contactInfoRepository.findById(2L)).thenReturn(Optional.ofNullable(contactInfo));
        doNothing().when(contactInfoRepository).delete(contactInfo);
        boolean isDeleted = contactInfoService.deleteContactInfo(2l);
        verify(contactInfoRepository, times(1)).delete(contactInfo);
        assertTrue(isDeleted);
    }
}
