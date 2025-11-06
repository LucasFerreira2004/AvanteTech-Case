package com.example.demo.integration.categoria;

import com.example.demo.auth.dto.LoginEnterDTO;
import com.example.demo.auth.tokens.TokenService;
import com.example.demo.categoria.Categoria;
import com.example.demo.categoria.CategoriaRepository;
import com.example.demo.categoria.dtos.CategoriaRegisterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UpdateCategoriaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TokenService tokenService;

    private static final String CATEGORIA_URL = "/categorias";
    private Categoria categoria;
    private String token;
    private LoginEnterDTO loginEnterDTO;

    @BeforeEach
    public void setup() {
        loginEnterDTO = new LoginEnterDTO("avante@gmail.com", "123");
        token = tokenService.generateToken(loginEnterDTO);

        categoria = new Categoria();
        categoria.setNome("Limpeza");
        categoria.setDescricao("produtos de limpeza");
        categoria.setAtivo(true);
        categoriaRepository.save(categoria);
    }

    @Test
    void ShoudUpdateCategoriaSuccefulyWhenFildsOk() throws Exception {
        CategoriaRegisterDTO registerDTO = new CategoriaRegisterDTO("alimentos", "alimentos deliciosos");
        String json = objectMapper.writeValueAsString(registerDTO);
        String url = CATEGORIA_URL + "/1";

        mockMvc.perform(put(url)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("alimentos"))
                .andExpect(jsonPath("$.descricao").value("alimentos deliciosos"));
    }

    @Test
    void ShoudUpdateCategoriaSuccefulyWhenFildsNotLowerCase() throws Exception {
        CategoriaRegisterDTO registerDTO = new CategoriaRegisterDTO("AlimentoS", "ALIMENTOS deliciosos");
        String json = objectMapper.writeValueAsString(registerDTO);
        String url = CATEGORIA_URL + "/1";

        mockMvc.perform(put(url)
                        .header("Authorization", "Bearer " + token)
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
        CategoriaRegisterDTO registerDTO = new CategoriaRegisterDTO("", null);
        String json = objectMapper.writeValueAsString(registerDTO);
        String url = CATEGORIA_URL + "/1";

        mockMvc.perform(put(url)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.field == 'nome')].message").value("O nome nao pode ser vazio"))
                .andExpect(jsonPath("$[?(@.field == 'descricao')].message").value("A descricao nao pode ser vazia"));
    }

    @Test
    void ShoudThrowExeptionWhenCategoryNotFound() throws Exception {
        CategoriaRegisterDTO registerDTO = new CategoriaRegisterDTO("alimentos", "alimentos deliciosos");
        String json = objectMapper.writeValueAsString(registerDTO);
        String url = CATEGORIA_URL + "/100";

        mockMvc.perform(put(url)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field").value("categoria"))
                .andExpect(jsonPath("$.message").value("recurso nao encontrado"));
    }
}
