package com.reece.address.book.manager.dao;

import com.reece.address.book.manager.entities.AddressBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressBookRepository extends JpaRepository<AddressBook,Long> {

    Page<AddressBook> findAll(Pageable page);
}
