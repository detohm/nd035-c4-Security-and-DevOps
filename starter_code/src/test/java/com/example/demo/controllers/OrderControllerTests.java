package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.Item;
import org.junit.Assert;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTests {
    private OrderController orderController;

    @Mock
    private UserRepository userRepository;
    @Mock
    private OrderRepository orderRepository;

    @Before
    public void setUp(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController,"userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }

    @Test
    public void verify_submit(){
        String username = "test";
        User user = new User();
        Item item1 = new Item();
        item1.setPrice(new BigDecimal("1"));
        Cart cart = new Cart();
        cart.addItem(item1);
        user.setCart(cart);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);

        ResponseEntity<UserOrder> res = orderController.submit(username);
        Assert.assertNotNull(res);
        Assert.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assert.assertNotNull(res.getBody());
        Assert.assertEquals(cart.getItems(), res.getBody().getItems());
        Assert.assertEquals(cart.getTotal(), res.getBody().getTotal());
        Assert.assertEquals(cart.getUser(), res.getBody().getUser());
    }

    @Test
    public void verify_submitNotFound(){
        String username = "test";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<UserOrder> res = orderController.submit(username);
        Assert.assertNotNull(res);
        Assert.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        Assert.assertNull(res.getBody());
    }

    @Test
    public void verify_getOrdersForUser(){
        String username = "test";
        User user = new User();
        List<UserOrder> userOrders = new ArrayList<>();
        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
        Mockito.when(orderRepository.findByUser(user)).thenReturn(userOrders);

        ResponseEntity<List<UserOrder>> res = orderController.getOrdersForUser(username);
        Assert.assertNotNull(res);
        Assert.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assert.assertEquals(userOrders, res.getBody());
    }

    @Test
    public void verify_getOrdersForUserNotFound(){
        String username = "test";
        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<List<UserOrder>> res = orderController.getOrdersForUser(username);
        Assert.assertNotNull(res);
        Assert.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        Assert.assertNull(res.getBody());
    }




}
