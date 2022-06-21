package com.product.io.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.product.io.model.Product;
import com.product.io.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RestTemplate restTemplate;


    @Spy
    @InjectMocks
    private ProductServiceImpl productService;

    private List<Product> productList;

    private Product product;

    @Value("${product.currency.baseCurrency}")
    String baseCurrency;

    String restResponse;

    @BeforeAll
    void setUp() {
        productList = Stream.of(
                new Product(01l, "product01", "product01 details", 1f),
                new Product(01l, "product02", "product02 details", 2f),
                new Product(01l, "product03", "product03 details", 3f),
                new Product(01l, "product04", "product04 details", 4f)
        ).collect(Collectors.toList());
        product = new Product(01l, "product04", "product04 details", 4f);
        restResponse="{\n" +
                "  \"base\": \"GBP\",\n" +
                "  \"date\": \"2022-06-21\",\n" +
                "  \"rates\": {\n" +
                "    \"EUR\": 1.165553,\n" +
                "    \"INR\": 95.965051,\n" +
                "    \"PKR\": 259.536824\n" +
                "  },\n" +
                "  \"success\": true,\n" +
                "  \"timestamp\": 1655848744\n" +
                "}";

    }

    @Test
    void getProductListTest() throws JsonProcessingException {
        when(productRepository.findAll()).thenReturn(productList);
        List<Product> productList = productService.getProductList();
        Assertions.assertNotNull(productList);
        Assertions.assertEquals(4, productList.size());
    }

    @Test
    void saveProductTest() {
        when(productRepository.save(any())).thenReturn(product);
        Product productSaved = productService.saveProduct(product);
        Assertions.assertNotNull(productSaved);
    }

    @Test
    void getDefaultCurrencyBaseTest(){
        ReflectionTestUtils.setField(productService, "baseCurrency", "GBP");
        Map<String, Float> map= productService.getDefaultCurrencyBase();
        Assertions.assertEquals("GBP",map.entrySet().stream().findFirst().get().getKey());
        Assertions.assertEquals(1f,map.entrySet().stream().findFirst().get().getValue());
    }
    @Test
    void controlApiCallTest(){
        ReflectionTestUtils.setField(productService, "baseURL", "https://api.apilayer.com/fixer/latest?");
        String upUrl=productService.controlApiCall("up");
        String downUrl=productService.controlApiCall("down");
        Assertions.assertEquals(upUrl,"https://api.apilayer.com/fixer/latest?");
        Assertions.assertEquals(downUrl,"https://api.apilayerxx.com/fixer/latest?");
    }
}
