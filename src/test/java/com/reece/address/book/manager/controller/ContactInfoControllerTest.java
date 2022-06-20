package com.reece.address.book.manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reece.address.book.manager.AddressBookServiceApplication;
import com.reece.address.book.manager.dto.ContactInfoDto;
import com.reece.address.book.manager.entities.ContactInfo;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AddressBookServiceApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureWebClient
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ContactInfoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    DataSource dataSource;

    @Test
    void getAllUniqueContacts() throws Exception {
        mockMvc.perform(get("/api/v1/contacts/all-unique")).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("[0].name").value("Reece Test1"))
                .andExpect(jsonPath("[0].contactNumber").value("+91-7983474073"))
                .andExpect(jsonPath("[1].name").value("Reece Test1"))
                .andExpect(jsonPath("[1].contactNumber").value("+91-7483474073"))
                .andExpect(jsonPath("[2].name").value("Reece Test2"))
                .andExpect(jsonPath("[2].contactNumber").value("+91-7554374073"))
                .andExpect(jsonPath("[3].name").value("Reece Test2"))
                .andExpect(jsonPath("[3].contactNumber").value("+91-7883474073"))
                .andExpect(jsonPath("[4].name").doesNotHaveJsonPath())
                .andExpect(jsonPath("[4].contactNumber").doesNotHaveJsonPath());
    }

    @Test
    void createContactInfo() throws Exception {

        ContactInfoDto contactInfoDto = new ContactInfoDto();
        contactInfoDto.setName("Reece Test1");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(contactInfoDto);
        mockMvc.perform(post("/api/v1/contacts/1").content(json)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.contactNumber").value("contactNumber can't be blank."));

        contactInfoDto.setContactNumber("+91-8736786363");
        json = objectMapper.writeValueAsString(contactInfoDto);
        mockMvc.perform(post("/api/v1/contacts/1").content(json)
                        .contentType("application/json"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("7"))
                .andExpect(jsonPath("$.contactNumber").value("+91-8736786363"));

        mockMvc.perform(get("/api/v1/addressbooks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.contactInfoList[2].contactNumber").value("+91-8736786363"));

        mockMvc.perform(post("/api/v1/contacts/14").content(json)
                .contentType("application/json"))
                .andExpect(status().isNotModified());

    }

    @Test
    void deleteAddressBook() throws Exception{

        mockMvc.perform(delete("/api/v1/contacts/4"))
                .andExpect(status().isAccepted());

        mockMvc.perform(delete("/api/v1/contacts/4"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/v1/contacts/12"))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/v1/contacts/*"))
                .andExpect(status().isBadRequest());
    }
}