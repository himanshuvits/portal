package com.travel.portal.controller;

import com.travel.portal.entity.UserEntity;
import com.travel.portal.model.UserModel;
import com.travel.portal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @Test
    void testGetAllUsers() {
        List<UserModel> expectedUsers = getUserModelList();
        when(userRepository.findAll()).thenReturn(getUserEntityList());
        ResponseEntity<List<UserModel>> actualUsers = userController.getAllUsers();
        verify(userRepository).findAll();
        assertNotNull(actualUsers.getBody(), "The returned user list should not be null");
        assertEquals(expectedUsers.size(), actualUsers.getBody().size(), "The size of returned user list should match expected");
        assertEquals(expectedUsers.getFirst().toString(), actualUsers.getBody().getFirst().toString(), "The returned user list should match the expected list");
    }

    private List<UserModel> getUserModelList() {
        return List.of(UserModel.builder()
                        .userName("John Doe")
                        .userId(1)
                        .userBand("A")
                        .userEmail("abc@def.com")
                        .build(),
                UserModel.builder()
                        .userName("Test User")
                        .userId(2)
                        .userBand("B")
                        .userEmail("test@user.com")
                        .build());
    }

    private List<UserEntity> getUserEntityList() {
        return List.of(
                new UserEntity(1, "John Doe", "abc@def.com", "A"),
                new UserEntity(2, "Test User", "test@user.com", "B")
        );
    }
}
