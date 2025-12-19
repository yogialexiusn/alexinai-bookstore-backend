package org.backend.mapper;


import org.backend.entity.User;
import org.backend.response.embedded.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse.DTO toUserDto(User user);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(UserResponse signUpDto);

}