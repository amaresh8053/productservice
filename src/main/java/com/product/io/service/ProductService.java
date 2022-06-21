package com.product.io.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.product.io.model.Product;
import com.product.io.model.ProductResponse;

import java.util.List;
import java.util.Map;

public interface ProductService {
    public List<Product> getProductList() throws JsonProcessingException;
    public Product getProduct(String id);
    public List<ProductResponse> getProductListWithCurrencies(Map<String, Float> rates) throws JsonProcessingException;
    public List<ProductResponse> processCurrency(List<Product> productList, Map<String, Float> rates) throws JsonProcessingException;
    public Map<String, Float> getCurrencyBase() throws JsonProcessingException;
    public Product saveProduct(Product product);
    public String controlApiCall(String value);
    public  Map<String, Float> getDefaultCurrencyBase();

}
