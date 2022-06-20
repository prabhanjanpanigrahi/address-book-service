package com.reece.address.book.manager.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.reece.address.book.manager.convertor.ObjectConvertor;
import com.reece.address.book.manager.dao.AddressBookRepository;
import com.reece.address.book.manager.dto.AddressBookDto;
import com.reece.address.book.manager.entities.AddressBook;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
public class AddressBookServiceTest {

    @Mock
    AddressBookRepository addressBookRepository;

    @Mock
    ObjectConvertor objectConvertor;

    @InjectMocks
    AddressBookService addressBookService;

    AddressBook addressBook;

    AddressBookDto addressBookDto;

    @BeforeEach
    public void setUpTestData() {
        addressBook = new AddressBook();
        addressBook.setName("Test1 Test1");
        addressBookDto = new AddressBookDto(1L,"Test1 Test1", null, null, null);
    }

    @Before("")
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void test_getAddressBooks() {
        List<AddressBook> addressBookSet = new ArrayList<>();
        addressBookSet.add(addressBook);
        Page<AddressBook> page = new PageImpl(addressBookSet);
        AddressBookDto addressBookDto = new AddressBookDto(1L,"Test1 Test1", null, null, null);
        Pageable pagable = PageRequest.of(0, 1, Sort.by("id").ascending());
        when(addressBookRepository.findAll(pagable)).thenReturn(page);
        when(objectConvertor.convertToAddressBookDto(addressBook)).thenReturn(addressBookDto);
        List<AddressBookDto> addressBookDtoList = addressBookService.getAddressBooks(0,1);
        verify(addressBookRepository, times(1)).findAll(pagable);
        assertEquals(1, addressBookDtoList.size());
    }

    @Test
    public void test_getAddressBookById() {
        when(addressBookRepository.findById(1L)).thenReturn(Optional.of(addressBook));
        when(objectConvertor.convertToAddressBookDto(addressBook)).thenReturn(addressBookDto);
        addressBookDto = addressBookService.getAddressBookById(1L);
        verify(addressBookRepository, times(1)).findById(1L);
        assertThat(addressBookDto).isNotNull();
    }

    @Test
    public void test_createAddressBook() {
        when(addressBookRepository.save(addressBook)).thenReturn(addressBook);
        when(objectConvertor.convertToAddressBookEntity(addressBookDto)).thenReturn(addressBook);
        when(objectConvertor.convertToAddressBookDto(addressBook)).thenReturn(addressBookDto);
        addressBookDto = addressBookService.createAddressBook(addressBookDto);
        verify(addressBookRepository, times(1)).save(addressBook);
        assertThat(addressBookDto).isNotNull();
    }

    @Test
    public void test_updateAddressBook() {
        AddressBook updatedAddressBook = new AddressBook();
        updatedAddressBook.setName("Test3 Test3");
        AddressBookDto updatedAddressBookDto = new AddressBookDto();
        updatedAddressBookDto.setName("Test3 Test3");
        when(addressBookRepository.findById(1L)).thenReturn(Optional.of(addressBook));
        when(addressBookRepository.save(addressBook)).thenReturn(updatedAddressBook);
        when(objectConvertor.convertToAddressBookDto(updatedAddressBook)).thenReturn(updatedAddressBookDto);
        addressBookDto = addressBookService.updateAddressBook(1L, updatedAddressBookDto);
        verify(addressBookRepository, times(1)).findById(1L);
        verify(addressBookRepository, times(1)).save(addressBook);
        assertEquals("Test3 Test3", updatedAddressBook.getName());
    }

    @Test
    public void test_deleteAddressBook() {
        when(addressBookRepository.findById(1L)).thenReturn(Optional.of(addressBook));
        doNothing().when(addressBookRepository).delete(addressBook);
        boolean isDeleted = addressBookService.deleteAddressBook(1L);
        verify(addressBookRepository, times(1)).delete(addressBook);
        assertTrue(isDeleted);
    }

}
