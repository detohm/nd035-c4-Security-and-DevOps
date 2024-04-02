package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTests {
    private UserController userController;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController,"userRepository", userRepository);
        TestUtils.injectObjects(userController,"cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
    }

    @Test
    public void verify_createUser(){
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test");
        req.setPassword("testPass");
        req.setConfirmPassword("testPass");

        ResponseEntity<User> res = userController.createUser(req);
        Assert.assertNotNull(res);
        Assert.assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    public void verify_createUserValidation(){
        CreateUserRequest req = new CreateUserRequest();
        req.setUsername("test");
        req.setPassword("testPass");
        req.setConfirmPassword("testPassXXX");

        ResponseEntity<User> res = userController.createUser(req);
        Assert.assertNotNull(res);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());

        req = new CreateUserRequest();
        req.setUsername("test");
        req.setPassword("test");
        req.setConfirmPassword("test");

        res = userController.createUser(req);
        Assert.assertNotNull(res);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, res.getStatusCode());
    }

    @Test
    public void verify_findByUserName(){
        String username = "test2";
        User user = createUser(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);

        ResponseEntity<User> res = userController.findByUserName(username);
        Assert.assertNotNull(res);
        Assert.assertEquals(user, res.getBody());
    }

    @Test
    public void verify_findById(){
        Long userId = 1L;
        User user = createUser("test");
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<User> res = userController.findById(userId);
        Assert.assertNotNull(res);
        Assert.assertEquals(user, res.getBody());
    }

    private User createUser(String username){
        User user = new User();
        user.setUsername(username);
        return user;
    }

}
