package com.reece.address.book.manager.controller;

import com.reece.address.book.manager.dto.AddressBookDto;
import com.reece.address.book.manager.entities.AddressBook;
import com.reece.address.book.manager.service.AddressBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/addressbooks")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @Operation(summary = "Get list of address books (Ordered by Id in ascending order), you must specify page number and page size")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation",
            content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @GetMapping
    public ResponseEntity<List<AddressBookDto>> getAddressBooks(@Parameter(description = "Page index (start from 0)")
                                                                    @RequestParam(value = "page") Integer page,
                                                                @Parameter(description = "Number of records per page")
                                                                @RequestParam(value = "pageSize") Integer pageSize) {
        return ResponseEntity.ok(addressBookService.getAddressBooks(page, pageSize));
    }

    @Operation(summary = "Get a Address Book by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the AddressBook",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AddressBook.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "AddressBook not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    @GetMapping("/{addressBookId}")
    public ResponseEntity<AddressBookDto> getAddressBook(@Parameter(description = "id of Address Book to be retrieved")
                                                      @PathVariable Long addressBookId) {
        AddressBookDto addressBookDto = addressBookService.getAddressBookById(addressBookId);
        if(addressBookDto != null) {
            return ResponseEntity.ok(addressBookDto);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Create a new addressBook")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "AddressBook created",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AddressBookDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid values supplied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @PostMapping
    public ResponseEntity<AddressBookDto> createAddressBook(@Valid @RequestBody AddressBookDto addressBookDto) {
        AddressBookDto createdAddressBook = addressBookService.createAddressBook(addressBookDto);
        if(createdAddressBook != null) {
            return new ResponseEntity(createdAddressBook, HttpStatus.CREATED);
        }
        return new ResponseEntity(HttpStatus.NOT_MODIFIED);
    }

    @Operation(summary = "Update a addressBook by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "addressBook updated",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AddressBookDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "AddressBook not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @PutMapping(value = "/{addressBookId}")
    public ResponseEntity<AddressBook> updateAddressBook(@Parameter(description = "id of addressBook to be updated")
                                                         @PathVariable Long addressBookId,
                                                         @Parameter(description = "addressBook updated information")
                                                         @Valid @RequestBody AddressBookDto addressBookDto) {
        AddressBookDto updatedAddressBookDto = addressBookService.updateAddressBook(addressBookId, addressBookDto);
        if(updatedAddressBookDto != null) {
            return new ResponseEntity(updatedAddressBookDto, HttpStatus.ACCEPTED);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    @Operation(summary = "Delete an addressbook by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "addressbook deleted", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "addressbook not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content) })
    @DeleteMapping(value = "/{addressBookId}")
    public ResponseEntity deleteAddressBook(@Parameter(description = "id of address book to be deleted")
                                            @PathVariable Long addressBookId){

        if(addressBookService.deleteAddressBook(addressBookId)) {
            return new ResponseEntity(HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}
