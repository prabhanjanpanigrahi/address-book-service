package com.reece.address.book.manager.service;

import com.reece.address.book.manager.convertor.ObjectConvertor;
import com.reece.address.book.manager.dao.AddressBookRepository;
import com.reece.address.book.manager.dao.ContactInfoRepository;
import com.reece.address.book.manager.dto.ContactInfoDto;
import com.reece.address.book.manager.entities.AddressBook;
import com.reece.address.book.manager.entities.ContactInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ContactInfoService {

    @Autowired
    ContactInfoRepository contactInfoRepository;

    @Autowired
    AddressBookRepository addressBookRepository;

    @Autowired
    ObjectConvertor objectConvertor;

    /**
     * This method is written to return unique contact info based on name and number throughout all address books
     * @return List<ContactInfoDto>
     */
    public List<ContactInfoDto> getAllUniqueContacts() {
        Set<ContactInfo> uniqueContactInfoSet = contactInfoRepository.getAllUniqueContacts();
        log.debug("In getAllUniqueContactNumbers uniqueContactInfoSet: "+uniqueContactInfoSet);
        return uniqueContactInfoSet.stream().map(objectConvertor::convertToContactInfoDto)
                .collect(Collectors.toList());
    }

    /**
     * This method is responsible to create a contact info for an address book
     * @param addressBookId Long
     * @param contactInfoDto ContactInfoDto
     * @return ContactInfoDto
     */
    public ContactInfoDto createContactInfo(Long addressBookId, ContactInfoDto contactInfoDto) {
        Optional<AddressBook> addressBook = addressBookRepository.findById(addressBookId);
        if (addressBook.isPresent()) {
            ContactInfo contactInfo = objectConvertor.convertToContactInfoEntity(contactInfoDto);
            contactInfo.setAddressBook(addressBook.get());
            contactInfo = contactInfoRepository.save(contactInfo);
            log.debug("In createContactInfo contactInfo: "+contactInfo
                    +" created successfully for addressBookId: "+addressBookId+" in system.");
            return objectConvertor.convertToContactInfoDto(contactInfo);
        }
        log.info("In createContactInfo addressBookId: "+addressBookId+" not found in system.");
        return null;
    }

    /**
     * This method is responsible to delete a contact info
     * @param contactInfoId Long
     * @return boolean
     */
    public boolean deleteContactInfo(Long contactInfoId) {
        Optional<ContactInfo> contactInfo = contactInfoRepository.findById(contactInfoId);
        if (contactInfo.isPresent()) {
            contactInfoRepository.delete(contactInfo.get());
            log.info("contactInfoId: "+contactInfoId+" deleted successfully from system.");
            return true;
        }
        log.info("In deleteContactInfo contactInfoId: "+contactInfoId+" not found in system.");
        return false;
    }
}
