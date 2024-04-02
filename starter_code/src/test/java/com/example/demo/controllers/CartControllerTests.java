package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTests {
    private CartController cartController;

    @Mock
    private UserRepository userRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController,"userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void verify_addToCart(){
        String username = "test";
        long itemId = 1L;
        User user = new User();
        Item item = new Item();
        item.setId(itemId);
        item.setName("item");
        item.setPrice(new BigDecimal("12.34"));
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        user.setCart(cart);

        ModifyCartRequest req = new ModifyCartRequest();
        req.setUsername(username);
        req.setItemId(itemId);
        req.setQuantity(2);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> res = cartController.addToCart(req);
        Assert.assertNotNull(res);
        Assert.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assert.assertNotNull(res.getBody());
        Assert.assertEquals(cart, res.getBody());
        Assert.assertNotNull(res.getBody().getItems());
        Assert.assertEquals(2, res.getBody().getItems().size());
        Assert.assertEquals(new BigDecimal("24.68"),res.getBody().getTotal());
    }

    @Test
    public void verify_addToCartNotFoundUser(){
        String username = "test";
        long itemId = 1L;

        ModifyCartRequest req = new ModifyCartRequest();
        req.setUsername(username);
        req.setItemId(itemId);
        req.setQuantity(2);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<Cart> res = cartController.addToCart(req);
        Assert.assertNotNull(res);
        Assert.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        Assert.assertNull(res.getBody());
    }

    @Test
    public void verify_addToCartNotFoundItem(){
        String username = "test";
        long itemId = 1L;
        User user = new User();

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        user.setCart(cart);

        ModifyCartRequest req = new ModifyCartRequest();
        req.setUsername(username);
        req.setItemId(itemId);
        req.setQuantity(2);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        ResponseEntity<Cart> res = cartController.addToCart(req);
        Assert.assertNotNull(res);
        Assert.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        Assert.assertNull(res.getBody());
    }

    @Test
    public void verify_removeFromCart(){
        String username = "test";
        long itemId = 1L;
        User user = new User();
        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("item");
        item1.setPrice(new BigDecimal("10"));
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("item");
        item2.setPrice(new BigDecimal("20"));

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>(Arrays.asList(item1,item2)));
        cart.setTotal(new BigDecimal("30"));
        user.setCart(cart);

        ModifyCartRequest req = new ModifyCartRequest();
        req.setUsername(username);
        req.setItemId(itemId);
        req.setQuantity(1);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(user);
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item1));

        ResponseEntity<Cart> res = cartController.removeFromCart(req);
        Assert.assertNotNull(res);
        Assert.assertEquals(HttpStatus.OK, res.getStatusCode());
        Assert.assertNotNull(res.getBody());
        Assert.assertEquals(cart, res.getBody());
        Assert.assertNotNull(res.getBody().getItems());
        Assert.assertEquals(1, res.getBody().getItems().size());
        Assert.assertEquals(new BigDecimal("20"),res.getBody().getTotal());
    }

    @Test
    public void verify_removeFromCartNotFoundUser(){
        String username = "test";
        long itemId = 1L;

        ModifyCartRequest req = new ModifyCartRequest();
        req.setUsername(username);
        req.setItemId(itemId);
        req.setQuantity(1);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        ResponseEntity<Cart> res = cartController.removeFromCart(req);
        Assert.assertNotNull(res);
        Assert.assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());
        Assert.assertNull(res.getBody());
    }
}
