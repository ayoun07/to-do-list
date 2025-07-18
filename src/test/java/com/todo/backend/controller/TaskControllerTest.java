package com.todo.backend.controller;

import com.todo.backend.model.ListEntity;
import com.todo.backend.model.Task;
import com.todo.backend.model.User;
import com.todo.backend.repository.ListRepository;
import com.todo.backend.repository.TaskRepository;
import com.todo.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskRepository taskRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private ListRepository listRepo;

    @Mock
    private Authentication authentication;

    private User mockUser;
    private ListEntity mockList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        mockList = new ListEntity();
        mockList.setId(1L);
        mockList.setName("Test List");
        mockList.setUser(mockUser);
    }

    @Test
    public void testGetTasks() {
        when(authentication.getName()).thenReturn("testuser");
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        Task task1 = new Task();
        task1.setTitle("Task 1");

        when(taskRepo.findByUser(mockUser)).thenReturn(List.of(task1));

        List<Task> result = taskController.getTasks(authentication);

        assertEquals(1, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
    }

    @Test
    public void testAddTask() {
        Task newTask = new Task();
        newTask.setTitle("New Task");

        when(authentication.getName()).thenReturn("testuser");
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(taskRepo.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Task savedTask = taskController.addTask(newTask, authentication);

        assertEquals("New Task", savedTask.getTitle());
        assertEquals(mockUser, savedTask.getUser());
        verify(taskRepo, times(1)).save(any(Task.class));
    }

    @Test
    public void testGetTasksByList_Authorized() {
        when(authentication.getName()).thenReturn("testuser");
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(listRepo.findById(1L)).thenReturn(Optional.of(mockList));

        Task task = new Task();
        task.setTitle("List Task");

        when(taskRepo.findByListAndUser(mockList, mockUser)).thenReturn(List.of(task));

        List<Task> result = taskController.getTasksByList(1L, authentication);

        assertEquals(1, result.size());
        assertEquals("List Task", result.get(0).getTitle());
    }
}
