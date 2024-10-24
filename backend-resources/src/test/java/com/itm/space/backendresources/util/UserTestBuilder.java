package com.itm.space.backendresources.util;

import com.itm.space.backendresources.api.request.UserRequest;
import com.itm.space.backendresources.api.response.UserResponse;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder(setterPrefix = "with")
public class UserTestBuilder {

    @Builder.Default
    private UUID uuid = UUID.fromString("ff5f593e-1b84-4765-adae-1f6097ba91cb");

    @Builder.Default
    private String username = "username";

    @Builder.Default
    private String firstName = "firstName";

    @Builder.Default
    private String lastName = "lastName";

    @Builder.Default
    private String email = "email@gmail.com";

    @Builder.Default
    private String password = "password";

    @Builder.Default
    private List<String> roles = List.of("default-roles-itm");

    @Builder.Default
    private List<String> groups = List.of("Moderators");

    public UUID buildUUID() {
        return uuid;
    }

    public UserResponse buildUserResponse() {
        return new UserResponse(firstName, lastName, email, roles, groups);
    }

    public UserRequest buildUserRequest() {
        return new UserRequest(username, email, password, firstName, lastName);
    }
}