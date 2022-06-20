package com.reece.address.book.manager.controller;

import com.reece.address.book.manager.dto.ContactInfoDto;
import com.reece.address.book.manager.service.ContactInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(value = "api/v1/contacts")
public class ContactInfoController {

    @Autowired
    private ContactInfoService contactInfoService;

    @Operation(summary = "Get unique set of all contacts across multiple address books order by contact name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Returned all unique set of contacts across multiple address books", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    @GetMapping(value = "/all-unique")
    ResponseEntity<Collection<ContactInfoDto>> getAllUniqueContactNumbers() {
        return ResponseEntity.ok(contactInfoService.getAllUniqueContacts());
    }

    @Operation(summary = "Create a new contact for a address book by addressId and contact info body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "contact info created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content),
            @ApiResponse(responseCode = "404", description = "address book not found to link the contact", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    @PostMapping(value = "/{addressBookId}")
    ResponseEntity<ContactInfoDto> createContactInfo(@Parameter(description = "id of addressBook to which this contact belongs")
                                                  @PathVariable Long addressBookId,
                                                  @Parameter(description = "ContactInfo information")
                                                  @Valid @RequestBody ContactInfoDto contactInfoDto) {
        ContactInfoDto createdContactInfo = contactInfoService.createContactInfo(addressBookId, contactInfoDto);
        if (createdContactInfo != null) {
            return new ResponseEntity(createdContactInfo, HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }

    @Operation(summary = "Delete a contact by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "contact info deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "contact info not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    @DeleteMapping(value = "/{contactInfoId}")
    ResponseEntity deleteContactInfo(@Parameter(description = "id of contact info which needs to be deleted")
                                     @PathVariable Long contactInfoId) {
        if (contactInfoService.deleteContactInfo(contactInfoId)) {
            return new ResponseEntity(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

}
