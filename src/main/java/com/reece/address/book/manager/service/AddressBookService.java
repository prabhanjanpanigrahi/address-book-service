package com.reece.address.book.manager.service;

import com.reece.address.book.manager.convertor.ObjectConvertor;
import com.reece.address.book.manager.dao.AddressBookRepository;
import com.reece.address.book.manager.dto.AddressBookDto;
import com.reece.address.book.manager.entities.AddressBook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AddressBookService {

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private ObjectConvertor objectConvertor;

    /**
     * This method gets page index and page size and returns records from database accordingly
     * @param page int
     * @param size int
     * @return List<AddressBookDto>
     */
    public List<AddressBookDto> getAddressBooks(int page, int size){
        List<AddressBook> addressBookList =  addressBookRepository.findAll(PageRequest.of(page, size,
                Sort.by("id").ascending())).toList();
        log.debug("In getAddressBooks method page: "+page+" size: "+size+" addressBookList: "+addressBookList);
        return addressBookList.stream().
                map(objectConvertor::convertToAddressBookDto).collect(Collectors.toList());
    }

    /**
     * This method gets an id and returns corresponding address book if it exists, otherwise it returns null
     * @param addressBookId Long
     * @return AddressBookDto
     */
    public AddressBookDto getAddressBookById(Long addressBookId){
        Optional<AddressBook> addressBook = addressBookRepository.findById(addressBookId);
        if (addressBook.isPresent()) {
            return objectConvertor.convertToAddressBookDto(addressBook.get());
        }
        log.info("In getAddressBookById method addressBookId: "+addressBookId+" not found in system.");
        return null;
    }

    /**
     * This method creates given address book object in the database and returns it with its Id
     * @param addressBookDto AddressBookDto
     * @return AddressBookDto
     */
    public AddressBookDto createAddressBook(AddressBookDto addressBookDto) {
        AddressBook savedAddressBook =  addressBookRepository.save(objectConvertor.convertToAddressBookEntity(addressBookDto));
        log.debug("Address Book: "+savedAddressBook+" created successfully in system.");
        return objectConvertor.convertToAddressBookDto(savedAddressBook);
    }

    /**
     * This method gets an address book object along with its id, updates the name and returns it back.
     * if the address book id doesn't exist, it returns null
     * @param addressBookId Long
     * @param changedAddressBookDto AddressBook
     * @return AddressBook
     */
    public AddressBookDto updateAddressBook(Long addressBookId, AddressBookDto changedAddressBookDto) {
        Optional<AddressBook> addressBook = addressBookRepository.findById(addressBookId);
        if (addressBook.isPresent()) {
            AddressBook tempAddressBook= addressBook.get();
            tempAddressBook.setName(changedAddressBookDto.getName());
            log.debug("In updateAddressBook addressBook: "+addressBook
                    +" updated successfully in system for addressBookId: "+addressBookId);
            return objectConvertor.convertToAddressBookDto(addressBookRepository.save(tempAddressBook));
        }
        log.info("In updateAddressBook addressBookId: "+addressBookId+" not found in system.");
        return null;
    }

    /**
     * This method gets an address book id and delete the corresponding address book record in database
     * if the address book id doesn't exist, it returns null
     * @param addressBookId Long
     * @return boolean
     */
    public boolean deleteAddressBook(Long addressBookId) {
        Optional<AddressBook> addressBook = addressBookRepository.findById(addressBookId);
        if (addressBook.isPresent()) {
            addressBookRepository.delete(addressBook.get());
            log.debug("addressBookId: "+addressBookId+" deleted successfully from system.");
            return true;
        }
        log.info("In deleteAddressBook addressBookId: "+addressBookId+" not found in system for deletion.");
        return false;
    }
}

