package com.example.demo.integration.categoria;

import com.example.demo.categoria.dtos.CategoriaRegisterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class CategoriaIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String CATEGORIA_URL = "/categorias";

    @Test
    void shouldReturn200WhenGetAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(CATEGORIA_URL))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void ShoudSaveCategoriaSuccefulyWhenFildsOk() throws Exception {
        CategoriaRegisterDTO registerDTO = new CategoriaRegisterDTO("alimentos", "alimentos deliciosos");
        String json = objectMapper.writeValueAsString(registerDTO);

        mockMvc.perform(post(CATEGORIA_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("alimentos"))
                .andExpect(jsonPath("$.descricao").value("alimentos deliciosos"));

    }
}
