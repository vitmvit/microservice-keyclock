package com.itm.space.backendresources.api.response;

import lombok.Data;

import java.util.List;

@Data
public class UserResponse {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final List<String> roles;
    private final List<String> groups;
}
