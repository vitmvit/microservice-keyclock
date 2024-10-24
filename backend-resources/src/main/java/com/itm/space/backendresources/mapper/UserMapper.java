package com.itm.space.backendresources.mapper;

import com.itm.space.backendresources.api.response.UserResponse;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = Collections.class)
public interface UserMapper {

    @Mapping(target = "roles", source = "roleList", qualifiedByName = "mapRoleRepresentationToString")
    @Mapping(target = "groups", source = "groupList", qualifiedByName = "mapGroupRepresentationToString")
    UserResponse userRepresentationToUserResponse(UserRepresentation userRepresentation,
                                                  List<RoleRepresentation> roleList,
                                                  List<GroupRepresentation> groupList);

    @Named("mapRoleRepresentationToString")
    default List<String> mapRoleRepresentationToString(List<RoleRepresentation> roleList) {
        return roleList.stream().map(RoleRepresentation::getName).toList();
    }

    @Named("mapGroupRepresentationToString")
    default List<String> mapGroupRepresentationToString(List<GroupRepresentation> groupList) {
        return groupList.stream().map(GroupRepresentation::getName).toList();
    }
}