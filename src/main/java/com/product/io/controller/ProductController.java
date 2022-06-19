package com.product.io.controller;

import com.product.io.model.Product;
import com.product.io.model.ProductResponse;
import com.product.io.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.product.io.util.Constant.GETTING_PRODUCT_LIST;
import static com.product.io.util.Constant.PRODUCT_INSERTED;


@RestController
public class ProductController {


    @Autowired
    ProductService productService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("v1/products")
    public ResponseEntity<List<Product>> getProducts(){
        try {
            List<Product> productList = productService.getProductList();
            logger.info(GETTING_PRODUCT_LIST,productList.size());
            return new ResponseEntity<>(productList, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("v2/products")
    public ResponseEntity<List<ProductResponse>> getProductsWithCurrency(){
        try {
            List<ProductResponse> ProductResponse = productService.getProductListWithCurrencies();
            logger.info(GETTING_PRODUCT_LIST,ProductResponse.size());
            return new ResponseEntity<>(ProductResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("v1/products")
    public ResponseEntity<Product> saveProduct(@RequestBody Product requestProduct) {
        try {
            Product product = productService.saveProduct(requestProduct);
            logger.info(PRODUCT_INSERTED,product.getName());
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

