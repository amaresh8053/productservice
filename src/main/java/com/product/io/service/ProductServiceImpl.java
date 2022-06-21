package com.product.io.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.io.model.Product;
import com.product.io.model.ProductResponse;
import com.product.io.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.product.io.util.Constant.*;

@Service
public class ProductServiceImpl implements ProductService{


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProductRepository productRepository;

    @Autowired
    RestTemplate restTemplate;

    ObjectMapper mapper = new ObjectMapper();

    @Value("${product.currency.baseCurrency}")
    private String baseCurrency;

    @Value("${product.currency.list}")
    private String currencyList;

    @Value("${product.currency.api.key}")
    private String apikey;

    @Value("${product.currency.api.url}")
    private String baseURL;

    @Override
    public List<Product> getProductList() throws JsonProcessingException {
        return productRepository.findAll();
    }

    @Override
    public List<ProductResponse> getProductListWithCurrencies(Map<String, Float> rates) throws JsonProcessingException {
        List<ProductResponse> productResponseList = new ArrayList<>();
        List<Product> productList = productRepository.findAll();
        productResponseList = processCurrency(productList, rates);
        return productResponseList;
    }

    @Override
    public List<ProductResponse> processCurrency(List<Product> productList, Map<String, Float> rates) throws JsonProcessingException {
        logger.info("Rates {}", rates);
        List<ProductResponse> productResponseList = new ArrayList<>();

        for (Product product : productList) {
            float priceInBaseCurrency = product.getPrice();
            Map<String, Float> updatedRates = rates.entrySet().stream().collect(Collectors.toMap(
                    e -> e.getKey(),
                    e -> e.getValue() * priceInBaseCurrency
            ));
            ProductResponse productResponse = new ProductResponse();
            BeanUtils.copyProperties(product, productResponse);
            productResponse.setPrice(updatedRates);
            productResponseList.add(productResponse);
        }

        return productResponseList;
    }

    @Override
    public Map<String, Float> getCurrencyBase() throws JsonProcessingException {
        logger.info(BASE_URL, baseURL);
        String URL = baseURL + SYMBOLS + currencyList + BASE + baseCurrency;
        logger.info(FINAL_URL, URL);
        HttpHeaders headers = new HttpHeaders();
        headers.set(APIKEY, apikey);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(URL, HttpMethod.GET, requestEntity, String.class);
        JsonNode jsonNode = mapper.readTree(response.getBody());
        Map<String, Float> rates = mapper.convertValue(jsonNode.get(RATES), new TypeReference<Map<String, Float>>() {
        });
        return rates;
    }

    public final Map<String, Float> getDefaultCurrencyBase() {
        Map<String, Float> rates = new HashMap<>();
        rates.put(baseCurrency, 1f);
        return rates;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public String controlApiCall(String value){
        if(value.equals(UP)){
            baseURL= BASEURL_UP;
        }if (value.equals(DOWN)){
            baseURL= BASEURL_DOWN;
        }
        return baseURL;
    }
}
