package com.fpt.usermanagement.service;

import com.fpt.usermanagement.dto.ApiResponse;
import com.fpt.usermanagement.dto.RegisterRequest;
import com.fpt.usermanagement.dto.UserResponse;
import com.fpt.usermanagement.entity.User;
import com.fpt.usermanagement.repository.UserRepository;
import com.fpt.usermanagement.security.CustomUserDetailsService;
import com.fpt.usermanagement.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailsService userDetailsService;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@fpt.edu.vn");
        testUser.setPassword("encodedPassword");
        testUser.setFullName("Test User");
        testUser.setRole(User.Role.STUDENT);
        testUser.setActive(true);
        testUser.setCreatedAt(LocalDateTime.now());

        registerRequest = new RegisterRequest();
        registerRequest.setUsername("newuser");
        registerRequest.setEmail("new@fpt.edu.vn");
        registerRequest.setPassword("password123");
        registerRequest.setFullName("New User");
        registerRequest.setRole(User.Role.STUDENT);
    }

    @Test
    void testRegister_WithValidData_ShouldSucceed() {
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        ApiResponse response = userService.register(registerRequest);

        assertTrue(response.getSuccess());
        assertEquals("User registered successfully!", response.getMessage());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_WithExistingUsername_ShouldFail() {
        when(userRepository.existsByUsername("newuser")).thenReturn(true);

        ApiResponse response = userService.register(registerRequest);

        assertFalse(response.getSuccess());
        assertEquals("Username already exists!", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetAllUsers_ShouldReturnUserList() {
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);

        List<UserResponse> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserById_WhenExists_ShouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserResponse result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateUser_WhenExists_ShouldSucceed() {
        RegisterRequest updateRequest = new RegisterRequest();
        updateRequest.setFullName("Updated Name");
        updateRequest.setEmail("updated@fpt.edu.vn");

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        ApiResponse response = userService.updateUser(1L, updateRequest);

        assertTrue(response.getSuccess());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser_WhenExists_ShouldSucceed() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        doNothing().when(userRepository).delete(any(User.class));

        ApiResponse response = userService.deleteUser(1L);

        assertTrue(response.getSuccess());
        verify(userRepository, times(1)).delete(any(User.class));
    }

    @Test
    void testToggleUserStatus_ShouldChangeStatus() {
        testUser.setActive(true);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        ApiResponse response = userService.toggleUserStatus(1L);

        assertTrue(response.getSuccess());
        verify(userRepository, times(1)).save(any(User.class));
    }
}