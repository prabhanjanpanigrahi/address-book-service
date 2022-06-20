package com.reece.address.book.manager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@NoArgsConstructor
@Setter
@Getter
public class ContactInfoDto {

    private Long id;

    @Size(min = 3, max = 100, message = "contact name should be minimum 3 characters")
    @NotBlank(message = "contact name can't be blank.")
    private String name;

    @NotBlank(message = "contactNumber can't be blank.")
    @Pattern(regexp="^\\+?[0-9]{2}-?[0-9]{10}$", message = "contactNumber should be starting with country code fallowed by 10 digits. " +
            "For eg. +91-7379973333")
    private String contactNumber;

    private LocalDateTime createDatetime;

    private LocalDateTime lastModifiedDatetime;

}
