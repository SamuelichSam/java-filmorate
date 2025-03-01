package ru.yandex.practicum.filmorate.model.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
    public static User toUser(UserDto userDto) {
        var user = new User();
        user.setId(userDto.id());
        user.setEmail(userDto.email());
        user.setLogin(userDto.login());
        user.setName(userDto.name());
        user.setBirthday(userDto.birthday());
        return user;
    }

    public static UserDto toDto(User user) {
        return new UserDto(
        user.getId(),
        user.getEmail(),
        user.getLogin(),
        user.getName(),
        user.getBirthday()
        );
    }
}
