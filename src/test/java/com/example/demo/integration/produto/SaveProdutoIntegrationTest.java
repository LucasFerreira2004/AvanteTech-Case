package com.example.demo.integration.produto;

import com.example.demo.auth.dto.LoginEnterDTO;
import com.example.demo.auth.tokens.TokenService;
import com.example.demo.categoria.Categoria;
import com.example.demo.categoria.CategoriaRepository;
import com.example.demo.produto.dtos.ProdutoRegisterDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class SaveProdutoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private TokenService tokenService;

    private static final String PRODUTO_URL = "/produtos";

    private Categoria categoria;
    private String token;
    private LoginEnterDTO loginEnterDTO;


    @BeforeEach
    void setup() {
        loginEnterDTO = new LoginEnterDTO("avante@gmail.com", "123");
        token = tokenService.generateToken(loginEnterDTO);

        categoria = new Categoria();
        categoria.setNome("comidas");
        categoria.setDescricao("belas comidas");
        categoria.setAtivo(true);
        categoriaRepository.save(categoria);

    }

    @Test
    void ShouldSaveProdutoSuccessfullyWhenFieldsOk() throws Exception {
        ProdutoRegisterDTO registerDTO = new ProdutoRegisterDTO(
                "Hambúrguer",
                "Delicioso hambúrguer",
                new BigDecimal("29.90"),
                categoria.getId()
        );
        String json = objectMapper.writeValueAsString(registerDTO);

        mockMvc.perform(post(PRODUTO_URL)
                        .header("Authorization", "Bearer " + token) // ✅ adiciona token
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("hambúrguer"))
                .andExpect(jsonPath("$.descricao").value("delicioso hambúrguer"))
                .andExpect(jsonPath("$.preco").value(29.90))
                .andExpect(jsonPath("$.categoriaId").value(categoria.getId()));
    }

    @Test
    void ShouldThrowExceptionWhenFieldsAreNullOrBlank() throws Exception {
        ProdutoRegisterDTO registerDTO = new ProdutoRegisterDTO(
                "",
                null,
                null,
                null
        );

        String json = objectMapper.writeValueAsString(registerDTO);

        mockMvc.perform(post(PRODUTO_URL)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.field == 'nome')].message").value("O nome nao pode ser vazio"))
                .andExpect(jsonPath("$[?(@.field == 'nome')].statusCode").value(400))
                .andExpect(jsonPath("$[?(@.field == 'descricao')].message").value("A descricao nao pode ser vazia"))
                .andExpect(jsonPath("$[?(@.field == 'descricao')].statusCode").value(400))
                .andExpect(jsonPath("$[?(@.field == 'preco')].message").value("o preco nao pode ser vazio"))
                .andExpect(jsonPath("$[?(@.field == 'preco')].statusCode").value(400))
                .andExpect(jsonPath("$[?(@.field == 'categoriaId')].message").value("o id da categoria nao pode ser vazio"))
                .andExpect(jsonPath("$[?(@.field == 'categoriaId')].statusCode").value(400));
    }

    @Test
    void ShouldThrowExceptionWhenCategoriaNotFound() throws Exception {
        ProdutoRegisterDTO registerDTO = new ProdutoRegisterDTO(
                "Pizza",
                "Pizza calabresa",
                new BigDecimal("59.90"),
                100L
        );

        String json = objectMapper.writeValueAsString(registerDTO);

        mockMvc.perform(post(PRODUTO_URL)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field").value("categoria"))
                .andExpect(jsonPath("$.message").value("recurso nao encontrado"));
    }

    @Test
    void ShouldThrowExceptionWhenCategoriaIsInactive() throws Exception {
        Categoria categoriaInativa = new Categoria();
        categoriaInativa.setNome("limpeza");
        categoriaInativa.setDescricao("produtos de limpeza");
        categoriaInativa.setAtivo(false);
        categoriaRepository.save(categoriaInativa);

        ProdutoRegisterDTO registerDTO = new ProdutoRegisterDTO(
                "Detergente",
                "Detergente neutro 500ml",
                new BigDecimal("5.00"),
                categoriaInativa.getId()
        );

        String json = objectMapper.writeValueAsString(registerDTO);

        mockMvc.perform(post(PRODUTO_URL)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field").value("categoria"))
                .andExpect(jsonPath("$.message").value("recurso nao encontrado"));
    }
}
