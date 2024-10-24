package com.itm.space.backendresources.controller;

import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.parent.BaseIntegrationTest;
import com.itm.space.backendresources.util.UserTestBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest extends BaseIntegrationTest {

    @Test
    @Order(1)
    @WithMockUser(roles = "MODERATOR")
    void helloShouldReturnAuthenticatedUserName() throws Exception {
        MvcResult result = mvc.perform(get("/api/users/hello"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        assertEquals("user", content);
    }

    @Test
    @Order(2)
    @WithMockUser(roles = "MODERATOR")
    void getUserByIdShouldReturnUserUuid() throws Exception {
        var uuid = UserTestBuilder.builder().build().buildUUID();

        mvc.perform(get("/api/users/{id}", uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("VitM"))
                .andExpect(jsonPath("$.lastName").value("Vit"))
                .andExpect(jsonPath("$.email").value("vit.m.vit.2002@gmail.com"));
    }

    @Test
    @Order(3)
    @WithMockUser(roles = "MODERATOR")
    void getUserByIdShould404WhenNotFound() throws Exception {
        String uuid = "user";

        mvc.perform(get("/api/users/{uuid}", uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @Order(4)
    @WithMockUser(roles = "MODERATOR")
    void createShouldReturn201WhenUserIsCreated() throws Exception {
        var userRequest = UserTestBuilder.builder().build().buildUserRequest();

        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isOk());
    }

    @Test
    @Order(5)
    @WithMockUser(roles = "MODERATOR")
    void createShouldReturn409WhenConflict() throws Exception {
        UserRequest userRequest = UserTestBuilder.builder().build().buildUserRequest();

        mvc.perform(requestWithContent(post("/api/users"), userRequest))
                .andExpect(status().isConflict());
    }
}