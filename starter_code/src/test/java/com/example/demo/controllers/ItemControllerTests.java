package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTests {
    private ItemController itemController;

    @Mock
    private ItemRepository itemRepository;

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository", itemRepository);
    }

    @Test
    public void verify_getItemById(){
        Long itemId = 1L;
        Item item = new Item();
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));

        ResponseEntity<Item> res = itemController.getItemById(itemId);
        Assert.assertNotNull(res);
        Assert.assertEquals(item, res.getBody());
        Assert.assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    public void verify_getItemsByName(){
        String itemName = "test";
        Item item = new Item();
        Mockito.when(itemRepository.findByName(itemName)).thenReturn(Collections.singletonList(item));

        ResponseEntity<List<Item>> res = itemController.getItemsByName(itemName);
        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getBody());
        Assert.assertEquals(1, res.getBody().size());
        Assert.assertEquals(item, res.getBody().get(0));
        Assert.assertEquals(HttpStatus.OK, res.getStatusCode());
    }

    @Test
    public void verify_getItems(){
        Item item1 = new Item();
        Item item2 = new Item();
        List<Item> items = Arrays.asList(item1,item2);
        Mockito.when(itemRepository.findAll()).thenReturn(items);

        ResponseEntity<List<Item>> res = itemController.getItems();
        Assert.assertNotNull(res);
        Assert.assertNotNull(res.getBody());
        Assert.assertEquals(items, res.getBody());
        Assert.assertEquals(HttpStatus.OK, res.getStatusCode());
    }

}
