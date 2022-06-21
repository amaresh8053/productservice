package com.product.io.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.product.io.model.Product;
import com.product.io.model.ProductResponse;
import com.product.io.service.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.product.io.util.Constant.*;


@RestController
public class ProductController {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    ProductService productService;

    @GetMapping("v1/products")
    public ResponseEntity<List<Product>> getProducts() {
        try {
            List<Product> productList = productService.getProductList();
            logger.info(GETTING_PRODUCT_LIST, productList.size());
            return new ResponseEntity<>(productList, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("v2/products")
    @CircuitBreaker(name = SERVICE_NAME,fallbackMethod = "getDefaultCurrencyBase")
    public ResponseEntity<List<ProductResponse>> getProductsWithCurrency() throws JsonProcessingException {
            Map<String, Float> rates = productService.getCurrencyBase();
            List<ProductResponse> ProductResponse = productService.getProductListWithCurrencies(rates);
            logger.info(GETTING_PRODUCT_LIST, ProductResponse.size());
            return new ResponseEntity<>(ProductResponse, HttpStatus.CREATED);
    }

    public ResponseEntity<List<ProductResponse>> getDefaultCurrencyBase(Exception ex) {
        try {
            Map<String, Float> rates = productService.getDefaultCurrencyBase();
            List<ProductResponse> ProductResponse = productService.getProductListWithCurrencies(rates);
            logger.info(GETTING_PRODUCT_LIST, ProductResponse.size());
            return new ResponseEntity<>(ProductResponse, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/currencyApi/{flag}")
    public ResponseEntity<String> controlApiCall(@PathVariable String flag){
        String result= productService.controlApiCall(flag);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping("v1/products")
    public ResponseEntity<Product> saveProduct(@RequestBody Product requestProduct) {
        try {
            Product product = productService.saveProduct(requestProduct);
            logger.info(PRODUCT_INSERTED, product.getName());
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

