package com.pehenriqueoliv.financecontrol.service;

import com.pehenriqueoliv.financecontrol.dto.request.UserRequest;
import com.pehenriqueoliv.financecontrol.dto.response.UserResponse;
import com.pehenriqueoliv.financecontrol.entity.User;
import com.pehenriqueoliv.financecontrol.exception.BusinessException;
import com.pehenriqueoliv.financecontrol.exception.ResourceNotFoundException;
import com.pehenriqueoliv.financecontrol.mapper.UserMapper;
import com.pehenriqueoliv.financecontrol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse create(UserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email already in use: " + request.email());
        }

        User user = userMapper.toEntity(request);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    public User findEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public void delete(Long id) {
        User user = findEntityById(id);
        userRepository.delete(user);
    }
}
