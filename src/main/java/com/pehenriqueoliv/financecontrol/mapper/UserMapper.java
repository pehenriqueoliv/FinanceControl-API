package com.pehenriqueoliv.financecontrol.mapper;

import com.pehenriqueoliv.financecontrol.dto.request.UserRequest;
import com.pehenriqueoliv.financecontrol.dto.response.UserResponse;
import com.pehenriqueoliv.financecontrol.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .build();
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
