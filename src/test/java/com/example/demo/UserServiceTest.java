package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void savesAndFindsAUser() {
        User user = new User("Priya", "priya@example.com");
        userService.saveUser(user);

        assertNotNull(user.getId(), "the database should assign an id");
        User found = userService.getUserById(user.getId());
        assertEquals("Priya", found.getName());
        assertEquals("priya@example.com", found.getEmail());
    }

    @Test
    void renamesAUser() {
        User user = new User("Arun", "arun@example.com");
        userService.saveUser(user);

        user.setName("Arun Kumar");
        userService.saveUser(user);

        assertEquals("Arun Kumar", userService.getUserById(user.getId()).getName());
    }

    @Test
    void deletesAUser() {
        User user = new User("Kavin", "kavin@example.com");
        userService.saveUser(user);

        userService.deleteUser(user.getId());

        assertNull(userService.getUserById(user.getId()));
    }

    @Test
    void unknownIdReturnsNull() {
        assertNull(userService.getUserById(999999L));
    }
}
