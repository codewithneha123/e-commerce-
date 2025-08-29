package com.neha.ecom_project.controller;

import com.neha.ecom_project.model.Product;
import com.neha.ecom_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")

public class ProductController {
    @Autowired
    private ProductService service;
    @RequestMapping("/")
    public String greet(){
        return "hello neha";
    }
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProduct(){
        return new ResponseEntity<>(service.getAllProducts(), HttpStatus.OK);

    }
    @GetMapping("/product/{id}")
    public ResponseEntity<Product>getProduct(@PathVariable int id){
        Product prod= service.getProductById(id);
        if(prod!=null){
            return new ResponseEntity<>(prod,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


    }
    @PostMapping("/product")
    public ResponseEntity<?> addProduct(@RequestPart Product product,
                                        @RequestPart MultipartFile imageFile){
        try{
            Product product1 = service.addProduct(product,imageFile);
            return  new ResponseEntity<>(product1,HttpStatus.CREATED);

        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/product/{productId}/image")
    public ResponseEntity<byte[]>getImageByProductId(@PathVariable int productId){

        Product product = service.getProductById(productId);
        byte[] imageFile= product.getImageData();
        return  ResponseEntity.ok().body(imageFile);
//                .contentType(MediaType.valueOf(product.getImageType()))


    }
    @PutMapping("/product/{Id}")
    public ResponseEntity<String> updateProduct(@PathVariable int Id,@RequestPart Product product,
        @RequestPart MultipartFile imageFile){
        Product prod = null;
        try {
            prod = service.updateProduct(Id, product,imageFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to update",HttpStatus.BAD_REQUEST);
        }
        if(prod!=null){
          return new ResponseEntity<>("Updated",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Failed to update",HttpStatus.BAD_REQUEST);
        }
    }
    @DeleteMapping("/product/{Id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int Id){
        Product prod = service.getProductById(Id);
        if(prod==null){
            return  new ResponseEntity<>("Product doesn't exist",HttpStatus.NOT_FOUND);
        }
        else {
            service.deleteProduct(Id);
            return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
        }
    }
    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProduct(@RequestParam String keyword){
        System.out.println("searching with "+keyword);
        List<Product> products= service.searchProducts(keyword);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    }


