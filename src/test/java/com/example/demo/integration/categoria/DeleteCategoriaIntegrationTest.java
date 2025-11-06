package com.example.demo.integration.categoria;

import com.example.demo.auth.dto.LoginEnterDTO;
import com.example.demo.auth.tokens.TokenService;
import com.example.demo.categoria.Categoria;
import com.example.demo.categoria.CategoriaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class DeleteCategoriaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenService tokenService;

    private static final String CATEGORIA_URL = "/categorias";
    private Categoria categoria;
    private String token;
    private LoginEnterDTO  loginEnterDTO;

    @BeforeEach
    void setup() {
        loginEnterDTO = new LoginEnterDTO("avante@gmail.com", "123");
        token = tokenService.generateToken(loginEnterDTO);

        categoria = new Categoria();
        categoria.setNome("Limpeza");
        categoria.setDescricao("Produtos de limpeza");
        categoria.setAtivo(true);
        categoriaRepository.save(categoria);
    }

    @Test
    void ShouldDeleteCategoriaSuccessfully() throws Exception {
        String url = CATEGORIA_URL + "/1";

        mockMvc.perform(delete(url)
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk());
    }

    @Test
    void ShouldThrowExceptionWhenCategoriaNotFound() throws Exception {
        String url = CATEGORIA_URL + "/100";

        mockMvc.perform(delete(url)
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field").value("categoria"))
                .andExpect(jsonPath("$.message").value("recurso nao encontrado"));
    }
}
