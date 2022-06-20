package com.reece.address.book.manager.entities;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "address_book")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressBook {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "addressBook", cascade = {CascadeType.ALL}, fetch = FetchType.LAZY)
    private List<ContactInfo> contactInfoList;

    @CreationTimestamp
    @Column(name="created_datetime")
    private LocalDateTime createDatetime;

    @UpdateTimestamp
    @Column(name="last_modified_datetime")
    private LocalDateTime lastModifiedDatetime;


    public void setContactInfoList(List<ContactInfo> contactInfoList) {
        this.contactInfoList = contactInfoList;
        for(ContactInfo contactInfo : contactInfoList) {
            contactInfo.setAddressBook(this);
        }
    }
}
