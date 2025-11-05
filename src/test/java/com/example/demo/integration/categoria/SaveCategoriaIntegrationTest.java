package com.example.demo.integration.categoria;

import com.example.demo.categoria.dtos.CategoriaRegisterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class SaveCategoriaIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String CATEGORIA_URL = "/categorias";

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

    @Test
    void ShoudSaveCategoriaSuccefulyWhenFildsNotLowerCase() throws Exception {
        CategoriaRegisterDTO registerDTO = new CategoriaRegisterDTO("AlimentoS", "ALIMENTOS deliciosos");
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

    @Test
    void ShoudThrowExeptionWhenFildsNullOrBlank() throws Exception {
        CategoriaRegisterDTO registerDTO = new CategoriaRegisterDTO("",null);
        String json = objectMapper.writeValueAsString(registerDTO);

        mockMvc.perform(post(CATEGORIA_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].field").value("descricao"))
                .andExpect(jsonPath("$[0].message").value("A descricao não pode ser vazia"))
                .andExpect(jsonPath("$[0].statusCode").value(400))
                .andExpect(jsonPath("$[0].error").value("Bad Request"))

                .andExpect(jsonPath("$[1].field").value("nome"))
                .andExpect(jsonPath("$[1].message").value("O nome nao pode ser vazio"))
                .andExpect(jsonPath("$[1].statusCode").value(400))
                .andExpect(jsonPath("$[1].error").value("Bad Request"));
    }

}
