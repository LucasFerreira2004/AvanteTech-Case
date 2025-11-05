package com.example.demo.integration.categoria;

import com.example.demo.categoria.Categoria;
import com.example.demo.categoria.dtos.CategoriaRegisterDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoriaIntegrationTest {
    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void ShoudSaveCategoriaSuccefuly(){
        CategoriaRegisterDTO registerDTO = new CategoriaRegisterDTO("alimentos", "alimentos deliciosos");
    }
}
