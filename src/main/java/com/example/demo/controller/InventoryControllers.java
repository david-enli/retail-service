package com.example.demo.controller;

import com.example.demo.model.ItemDTO;
import com.example.demo.model.NewOrderDTO;
import com.example.demo.model.OrderDTO;
import com.example.demo.model.ReviewDTO;
import com.example.demo.service.InventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Api(value = "Retail Management System", description = "Operations pertaining to retailing")
@RestController
public class InventoryControllers {
    @Autowired
    InventoryService inventoryService;

    @ApiOperation(value = "Add inventories")
    @RequestMapping(value = "/addInventory", method = RequestMethod.POST)
    public long addInventory(@RequestBody ItemDTO item) throws Exception {

        return inventoryService.addItem(item).getId();
    }

    // search the inventory given name
    @ApiOperation(value = "retrieve items given name containing the given name string")
    @RequestMapping(value = "/getItems", method = RequestMethod.GET)
    public List<?> getItems(@ApiParam(value = "given item name", required = true)@RequestParam String itemName) throws Exception {
        return inventoryService.findItem(itemName);
    }

    @ApiOperation(value = "retrieve an inventory based on given itemId")
    @RequestMapping(value = "/getInventory", method = RequestMethod.GET)
    public ItemDTO getInventory(@ApiParam(value = "given itemId", required = true)@RequestParam Integer itemId) throws Exception {
        return inventoryService.findInventory(itemId);
    }



    @ApiOperation(value = "post a review")
    @RequestMapping(value = "/postReview", method = RequestMethod.POST)
    public void postReview(@RequestBody ReviewDTO review) throws Exception {
        inventoryService.addReview(review);
    }

    @ApiOperation(value = "retrieve all reviews for an item")
    @RequestMapping(value = "getReview", method = RequestMethod.GET)
    public List<ReviewDTO> getReview(@ApiParam(value = "given itemId", required = true)@RequestParam Integer itemId) throws Exception {
        List<ReviewDTO> list = new ArrayList<>();
        String temp = inventoryService.retrieveReview(itemId) ;
        String[] results = temp.split("\\\\r\\\\n");
        for(int i = 0; i < results.length; i++) {
            String result = results[i];
            list.add(new ReviewDTO(itemId, Integer.parseInt(result.substring(0, 1)), result.substring(1)));
        }
        return list;
    }

    @ApiOperation(value = "place order")
    @RequestMapping(value = "placeOrder", method = RequestMethod.POST)
    public void placeOrder(@RequestBody NewOrderDTO order) throws Exception {

        inventoryService.placeOrder(order);
    }

    @ApiOperation(value = "return all items for a given category")
    @RequestMapping(value= "getItemsOnCategory", method = RequestMethod.GET)
    public List<String> getTopItems(@ApiParam(value = "given item category", required = true)@RequestParam String category) throws Exception {
        return inventoryService.getTopItems(category);
    }


}
