package com.reece.address.book.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.address.book.manager.AddressBookServiceApplication;
import com.reece.address.book.manager.dto.AddressBookDto;
import com.reece.address.book.manager.dto.ContactInfoDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AddressBookServiceApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureWebClient
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AddressBookControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DataSource dataSource;

    @Test
    void getAddressBooks() throws Exception {
        mockMvc.perform(get("/api/v1/addressbooks?page=0&pageSize=2")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json")).andExpect(jsonPath("[0].id").value("1"))
                .andExpect(jsonPath("[1].id").value("2"));

        mockMvc.perform(get("/api/v1/addressbooks?page=0")).andExpect(status().isBadRequest());
    }

    @Test
    void getAddressBookById() throws Exception {

        mockMvc.perform(get("/api/v1/addressbooks/1")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test1 Test1"));

        mockMvc.perform(get("/api/v1/addressbooks/16"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/v1/addressbooks/i"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void createAddressBook() throws Exception{

        AddressBookDto addressBookDto = new AddressBookDto();
        addressBookDto.setName("Test4 Test4");
        List<ContactInfoDto> contactInfoDtoList = new ArrayList();
        ContactInfoDto contactInfoDto = new ContactInfoDto();
        contactInfoDto.setContactNumber("49943734983");
        contactInfoDto.setName("Reece Test4");
        contactInfoDtoList.add(contactInfoDto);
        addressBookDto.setContactInfoList(contactInfoDtoList);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(addressBookDto);
        mockMvc.perform(post("/api/v1/addressbooks").content(json)
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("4"))
                .andExpect(jsonPath("$.name").value("Test4 Test4"))
                .andExpect(jsonPath("$.contactInfoList[0].contactNumber").value("49943734983"));
    }

    @Test
    void updateAddressBook() throws Exception{

        AddressBookDto addressBookDto = new AddressBookDto();
        addressBookDto.setId(2l);
        addressBookDto.setName("Test3 Test3");
        addressBookDto.setContactInfoList(new ArrayList<>());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(addressBookDto);
        mockMvc.perform(put("/api/v1/addressbooks/2").content(json)
                        .contentType("application/json"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.name").value("Test3 Test3"));

        mockMvc.perform(put("/api/v1/addressbooks/16").content(json)
                .contentType("application/json"))
                .andExpect(status().isNotFound());

        mockMvc.perform(put("/api/v1/addressbooks/*").content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteAddressBook() throws Exception{

        mockMvc.perform(delete("/api/v1/addressbooks/2"))
                .andExpect(status().isAccepted());

        mockMvc.perform(delete("/api/v1/addressbooks/2"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/v1/addressbooks/12"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/v1/addressbooks/*"))
                .andExpect(status().isBadRequest());
    }
}