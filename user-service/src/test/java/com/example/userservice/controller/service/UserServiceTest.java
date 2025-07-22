package com.example.userservice.controller.service;

import com.example.userservice.domain.UserDTO;
import com.example.userservice.entity.User;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    private UserDTO sampleDTO;
    private User sampleUser;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        sampleDTO = new UserDTO(1L, "admin", "password", "ADMIN");
        sampleUser = new User(1L, "admin", "encodedPass", "ADMIN");
    }

    @Test
    void testRegister() {
        when(userMapper.toEntity(sampleDTO)).thenReturn(sampleUser);
        when(passwordEncoder.encode("password")).thenReturn("encodedPass");
        when(userRepo.save(sampleUser)).thenReturn(sampleUser);
        when(userMapper.toDto(sampleUser)).thenReturn(sampleDTO);

        userService.register(sampleDTO);

        verify(userRepo).save(sampleUser);
    }

    @Test
    void testValidateUser_Valid() {
        when(userRepo.findByUsername("admin")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("password", "encodedPass")).thenReturn(true);

        Optional<User> result = userService.validate("admin", "password");

        assertThat(result).isPresent().contains(sampleUser);
    }

    @Test
    void testValidateUser_Invalid() {
        when(userRepo.findByUsername("admin")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("wrong", "encodedPass")).thenReturn(false);

        Optional<User> result = userService.validate("admin", "wrong");

        assertThat(result).isEmpty();
    }

    @Test
    void testGetByUsername() {
        when(userRepo.findByUsername("admin")).thenReturn(Optional.of(sampleUser));
        when(userMapper.toDto(sampleUser)).thenReturn(sampleDTO);

        Optional<UserDTO> result = userService.getByUsername("admin");

        assertThat(result).isPresent().contains(sampleDTO);
    }

    @Test
    void testGetLoggedInUserProfile() {
        Authentication auth = mock(Authentication.class);
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        when(auth.getName()).thenReturn("admin");

        SecurityContextHolder.setContext(context);

        when(userRepo.findByUsername("admin")).thenReturn(Optional.of(sampleUser));
        when(userMapper.toDto(sampleUser)).thenReturn(sampleDTO);

        UserDTO result = userService.getLoggedInUserProfile();

        assertThat(result).isEqualTo(sampleDTO);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = List.of(sampleUser);
        when(userRepo.findAll()).thenReturn(users);
        when(userMapper.toDto(sampleUser)).thenReturn(sampleDTO);

        List<UserDTO> result = userService.getAllUsers();

        assertThat(result).containsExactly(sampleDTO);
    }

    @Test
    void testGetUserById() {
        when(userRepo.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(userMapper.toDto(sampleUser)).thenReturn(sampleDTO);

        UserDTO result = userService.getUserById(1L);

        assertThat(result).isEqualTo(sampleDTO);
    }

    @Test
    void testUpdateUser() {
        UserDTO updatedDTO = new UserDTO(1L, "updatedUser", "newPass", "USER");
        User updatedUser = new User(1L, "updatedUser", "encodedPass", "USER");

        when(userRepo.findById(1L)).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedPass");
        when(userRepo.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(updatedDTO);

        UserDTO result = userService.updateUser(1L, updatedDTO);

        assertThat(result).isEqualTo(updatedDTO);
    }

    @Test
    void testDeleteUser() {
        when(userRepo.existsById(1L)).thenReturn(true);
        doNothing().when(userRepo).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepo).deleteById(1L);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepo.existsById(2L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(2L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }
}
