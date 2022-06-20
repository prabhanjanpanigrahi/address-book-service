package com.reece.address.book.manager.dao;

import com.reece.address.book.manager.entities.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ContactInfoRepository extends JpaRepository<ContactInfo,Long> {

    @Query(value = "select c from ContactInfo c order by c.name")
    Set<ContactInfo> getAllUniqueContacts();
}
