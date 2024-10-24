package com.itm.space.backendresources.service;

import com.itm.space.backendresources.exception.BackendResourcesException;
import com.itm.space.backendresources.mapper.UserMapper;
import com.itm.space.backendresources.service.impl.UserServiceImpl;
import com.itm.space.backendresources.util.UserTestBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.MappingsRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private Keycloak keycloakClient;

    @MockBean
    private UserMapper userMapper;

    private UsersResource mockedUsersResource;

    @BeforeEach
    public void setUp() {
        var mockedRealmResource = mock(RealmResource.class);
        mockedUsersResource = mock(UsersResource.class);

        when(keycloakClient.realm(any())).thenReturn(mockedRealmResource);
        when(mockedRealmResource.users()).thenReturn(mockedUsersResource);
    }

    @Test
    public void createShouldCreateUser() {
        var userRequest = UserTestBuilder.builder().build().buildUserRequest();
        var mockResponse = mock(Response.class);

        when(mockResponse.getStatus()).thenReturn(201);
        when(mockResponse.getStatusInfo()).thenReturn(Response.Status.CREATED);
        when(mockedUsersResource.create(any(UserRepresentation.class))).thenReturn(mockResponse);

        userService.createUser(userRequest);

        verify(mockedUsersResource, times(1)).create(any(UserRepresentation.class));
    }

    @Test
    public void createShouldReturnException() {
        var userRequest = UserTestBuilder.builder().build().buildUserRequest();

        when(mockedUsersResource.create(any(UserRepresentation.class))).thenThrow(new WebApplicationException("Something went wrong", Response.Status.INTERNAL_SERVER_ERROR));

        var thrown = assertThrows(BackendResourcesException.class, () -> userService.createUser(userRequest));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getHttpStatus());
        verify(mockedUsersResource, times(1)).create(any(UserRepresentation.class));
    }

    @Test
    public void getUserByIdShouldReturnUserByUuid() {
        var userRequest = UserTestBuilder.builder().build().buildUserResponse();
        var uuid = UserTestBuilder.builder().build().buildUUID();
        var mockUserResource = mock(UserResource.class);
        var mockUserRepresentation = new UserRepresentation();
        var mockRoles = new ArrayList<RoleRepresentation>();
        var mockGroups = new ArrayList<GroupRepresentation>();
        var mockRoleMappingResource = mock(RoleMappingResource.class);
        var mockMappingsRepresentation = mock(MappingsRepresentation.class);

        when(mockedUsersResource.get(uuid.toString())).thenReturn(mockUserResource);
        when(mockUserResource.toRepresentation()).thenReturn(mockUserRepresentation);
        when(mockUserResource.roles()).thenReturn(mockRoleMappingResource);
        when(mockRoleMappingResource.getAll()).thenReturn(mockMappingsRepresentation);
        when(mockMappingsRepresentation.getRealmMappings()).thenReturn(mockRoles);
        when(mockUserResource.groups()).thenReturn(mockGroups);
        when(userMapper.userRepresentationToUserResponse(mockUserRepresentation, mockRoles, mockGroups)).thenReturn(userRequest);

        var userResponse = userService.getUserById(uuid);

        assertNotNull(userResponse);
        verify(mockedUsersResource, times(3)).get(uuid.toString());
    }

    @Test
    public void getUserByIdShouldReturnException() {
        var uuid = UserTestBuilder.builder().build().buildUUID();

        when(mockedUsersResource.get(uuid.toString())).thenThrow(new RuntimeException("Not found"));

        var thrown = assertThrows(BackendResourcesException.class, () -> userService.getUserById(uuid));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, thrown.getHttpStatus());
        verify(mockedUsersResource).get(uuid.toString());
    }
}