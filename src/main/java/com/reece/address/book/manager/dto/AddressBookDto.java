package com.reece.address.book.manager.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressBookDto {

    private Long id;

    @NotBlank(message = "address book name can't be blank.")
    @Size(min = 3, max = 100, message = "address book name should be minimum 3 characters")
    private String name;

    private List<ContactInfoDto> contactInfoList;

    private LocalDateTime createDatetime;

    private LocalDateTime lastModifiedDatetime;

}
