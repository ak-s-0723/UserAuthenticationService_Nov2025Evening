package org.example.userauthservice_nov2025evening.utils.mappers;

import org.example.userauthservice_nov2025evening.dtos.UserDto;
import org.example.userauthservice_nov2025evening.models.User;

public class UserDtoMapper {
    public static UserDto from(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setRoles(user.getRoles());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static User from(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        return user;
    }

}
