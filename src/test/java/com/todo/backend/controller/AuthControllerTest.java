package com.todo.backend.controller;

import com.todo.backend.dto.RegisterRequest;
import com.todo.backend.model.User;
import com.todo.backend.repository.UserRepository;
import com.todo.backend.security.JwtUtil;
import com.todo.backend.service.CustomUserDetailsService;
import com.todo.backend.service.EmailService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    private MockMvc mockMvc;

    private User mockUser;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setPassword("encodedPassword");
        mockUser.setEnabled(true);
    }

    @Test
    public void testRegister_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@email.com");
        request.setPassword("password123");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("newuser@email.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");

        ResponseEntity<?> response = authController.register(request);

        assertEquals(200, response.getStatusCodeValue());
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendConfirmationEmail(eq("newuser@email.com"), anyString());
    }

    @Test
    public void testRegister_UsernameExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");
        request.setEmail("user@email.com");
        request.setPassword("password");

        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(new User()));

        ResponseEntity<?> response = authController.register(request);

        assertEquals(400, response.getStatusCodeValue());
        verify(userRepository, never()).save(any());
        verify(emailService, never()).sendConfirmationEmail(any(), any());
    }

    @Test
    public void testRegister_EmailExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("uniqueuser");
        request.setEmail("taken@email.com");
        request.setPassword("password");

        when(userRepository.findByUsername("uniqueuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("taken@email.com")).thenReturn(Optional.of(new User()));

        ResponseEntity<?> response = authController.register(request);

        assertEquals(400, response.getStatusCodeValue());
        verify(userRepository, never()).save(any());
        verify(emailService, never()).sendConfirmationEmail(any(), any());
    }

    @Test
    public void testLogin_Success() throws Exception {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(customUserDetailsService.loadUserByUsername("testuser")).thenReturn(
                org.springframework.security.core.userdetails.User
                        .withUsername("testuser")
                        .password("encodedPassword")
                        .authorities("USER")
                        .build()
        );
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("mockJwtToken");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testuser\", \"password\": \"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"token\": \"mockJwtToken\"}"));
    }

    @Test
    public void testLogin_UserNotEnabled() throws Exception {
        mockUser.setEnabled(false);
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testuser\", \"password\": \"password123\"}"))
                .andExpect(status().isForbidden())
                .andExpect(content().string("Veuillez confirmer votre adresse e-mail avant de vous connecter."));
    }

    @Test
    public void testLogin_InvalidCredentials() throws Exception {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager)
                .authenticate(any());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"testuser\", \"password\": \"wrongPassword\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Identifiants invalides."));
    }
}
