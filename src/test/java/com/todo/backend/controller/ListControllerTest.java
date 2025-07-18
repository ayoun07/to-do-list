package com.todo.backend.controller;

import com.todo.backend.model.ListEntity;
import com.todo.backend.model.User;
import com.todo.backend.repository.ListRepository;
import com.todo.backend.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ListControllerTest {

    @InjectMocks
    private ListController listController;

    @Mock
    private ListRepository listRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private Authentication authentication;

    @Mock
    private Principal principal;

    private User mockUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
    }

    @Test
    public void testGetUserLists() {
        ListEntity list = new ListEntity();
        list.setName("Courses");

        when(authentication.getName()).thenReturn("testuser");
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(listRepo.findByUser(mockUser)).thenReturn(List.of(list));

        List<ListEntity> result = listController.getUserLists(authentication);

        assertEquals(1, result.size());
        assertEquals("Courses", result.get(0).getName());
    }

    @Test
    public void testCreateList() {
        ListEntity newList = new ListEntity();
        newList.setName("Travail");

        when(authentication.getName()).thenReturn("testuser");
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(listRepo.save(any(ListEntity.class))).thenAnswer(i -> i.getArgument(0));

        ResponseEntity<?> response = listController.createList(newList, authentication);

        assertEquals(200, response.getStatusCodeValue());
        ListEntity savedList = (ListEntity) response.getBody();
        assertEquals("Travail", savedList.getName());
        assertEquals(mockUser, savedList.getUser());
    }

    @Test
    public void testDeleteList_Success() {
        ListEntity list = new ListEntity();
        list.setId(1L);
        list.setUser(mockUser);

        when(listRepo.findById(1L)).thenReturn(Optional.of(list));
        when(principal.getName()).thenReturn("testuser");

        ResponseEntity<?> response = listController.deleteList(1L, principal);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Liste supprimée.", response.getBody());
        verify(listRepo, times(1)).delete(list);
    }

    @Test
    public void testDeleteList_Forbidden() {
        User otherUser = new User();
        otherUser.setUsername("otheruser");

        ListEntity list = new ListEntity();
        list.setId(2L);
        list.setUser(otherUser);

        when(listRepo.findById(2L)).thenReturn(Optional.of(list));
        when(principal.getName()).thenReturn("testuser");

        ResponseEntity<?> response = listController.deleteList(2L, principal);

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Accès interdit.", response.getBody());
    }

    @Test
    public void testUpdateList_Success() {
        ListEntity list = new ListEntity();
        list.setId(1L);
        list.setName("Old Name");
        list.setUser(mockUser);

        ListEntity updatedList = new ListEntity();
        updatedList.setName("New Name");

        when(listRepo.findById(1L)).thenReturn(Optional.of(list));
        when(principal.getName()).thenReturn("testuser");
        when(listRepo.save(any(ListEntity.class))).thenReturn(list);

        ResponseEntity<?> response = listController.updateList(1L, updatedList, principal);

        assertEquals(200, response.getStatusCodeValue());
        ListEntity result = (ListEntity) response.getBody();
        assertEquals("New Name", result.getName());
    }

    @Test
    public void testUpdateList_NotFound() {
        when(listRepo.findById(99L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = listController.updateList(99L, new ListEntity(), principal);

        assertEquals(404, response.getStatusCodeValue());
    }
}

