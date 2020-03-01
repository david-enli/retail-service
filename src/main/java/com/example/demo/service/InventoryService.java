package com.example.demo.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.demo.dao.InventoryDao;
import com.example.demo.model.*;
import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService {
    @Autowired
    private InventoryDao inventoryDao;

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${service.url.orderService}")
    private String orderServiceBaseUri;

    @Value("${service.url.paymentService}")
    private String paymentServiceBaseUrl;

    @Value("${amazon.s3.bucketName}")
    private String bucketName;

    private RestTemplate restTemplate = new RestTemplate();;

    public DAOItem addItem(ItemDTO item) {
        DAOItem newItem = new DAOItem();
        newItem.setCategory(item.getCategory());
        newItem.setItemName(item.getItemName());
        newItem.setPrice(item.getPrice());
        newItem.setDescription(item.getDescription());
        newItem.setSellerName(item.getSellerName());
        newItem.setQuantity(item.getQuantity());
        return inventoryDao.save(newItem);
    }

    public List<DAOItem> findItem(String itemName) {
        return inventoryDao.findByItemNameContaining(itemName);
    }

    public ItemDTO findInventory(Integer itemId) {
        DAOItem daoItem = inventoryDao.findOneById(itemId);
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setCategory(daoItem.getCategory());
        itemDTO.setDescription(daoItem.getDescription());
        itemDTO.setItemName(daoItem.getItemName());
        itemDTO.setPrice(daoItem.getPrice());
        itemDTO.setQuantity(daoItem.getQuantity());
        itemDTO.setSellerName(daoItem.getSellerName());
        return itemDTO;
    }

    public void addReview(ReviewDTO reviews) throws Exception{
        String newFileName = reviews.getItemID() + ".txt";
        String existingReview = retrieveReview(reviews.getItemID());
        try {
            File file = new File(newFileName);
            file.createNewFile();
            FileWriter myWriter = new FileWriter(newFileName);
            myWriter.write(existingReview);
            myWriter.write(reviews.getRating()+reviews.getReviewText() + "\\r\\n");
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            AccessControlList acl = new AccessControlList();
            acl.grantPermission(GroupGrantee.AllUsers, Permission.Read); //all users or authenticated

            PutObjectRequest putRequest1 = new PutObjectRequest(bucketName, newFileName, file).withCannedAcl(CannedAccessControlList.PublicRead);
            putRequest1.setAccessControlList(acl);
            PutObjectResult response1 = amazonS3.putObject(putRequest1);
            System.out.println("Uploaded object encryption status is " +
                    response1.getSSEAlgorithm());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    public String retrieveReview(Integer itemID) throws Exception{
        String key = itemID + ".txt";
        System.out.println("Downloading an object");
        String result = "";
        S3Object fullObject;
        if(amazonS3.doesObjectExist(bucketName, key)){
            fullObject = amazonS3.getObject(new GetObjectRequest(bucketName, key));
        } else {
            return result;
        }

        return displayTextInputStream(fullObject.getObjectContent());
    }

    private String displayTextInputStream(InputStream input) throws IOException {
        // Read the text input stream one line at a time and display each line.
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String line = null;
        StringBuilder sb = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    public void placeOrder(NewOrderDTO newOrderDTO) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setItemId(newOrderDTO.getItemId());
        orderDTO.setQuantity(newOrderDTO.getQuantity());
        orderDTO.setUserId(newOrderDTO.getUserId());
        DAOItem item = inventoryDao.findOneById(newOrderDTO.getItemId());
        orderDTO.setCategory(item.getCategory());
        orderDTO.setOrderName(item.getItemName());
        orderDTO.setUnitPrice(item.getPrice());
        if(item.getQuantity() < newOrderDTO.getQuantity()) {

            orderDTO.setStatus("OutOfStock");
        }
        //subtract inventory
        orderDTO.setStatus("Placed");
        ////insert an order into DynamoDB

        String orderID = restTemplate.postForObject(orderServiceBaseUri,orderDTO, String.class);


        MakePaymentDTO makePaymentDTO = new MakePaymentDTO();
        makePaymentDTO.setOrderId(orderID);
        makePaymentDTO.setPaymentAmount(item.getPrice() * newOrderDTO.getQuantity());
        makePaymentDTO.setUserId(newOrderDTO.getUserId());


        ResponseEntity result = restTemplate.postForEntity(paymentServiceBaseUrl, makePaymentDTO, String.class);

        if(result.getStatusCode().is2xxSuccessful()) {
            item.setQuantity(item.getQuantity()-newOrderDTO.getQuantity());
            inventoryDao.save(item);
        }

        //create shipment request;


       // System.out.println(result);
    }

    public List<String> getTopItems(String category) {
        List<DAOItem> list = inventoryDao.findByCategory(category);
        List<String> result = new ArrayList<>();
        for(int i = 0; i < list.size(); i++ ) {
            result.add(list.get(i).getItemName());
        }
        return result;
    }

}
