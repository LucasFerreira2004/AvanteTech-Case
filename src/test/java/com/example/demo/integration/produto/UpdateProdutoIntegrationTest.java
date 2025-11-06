package com.example.demo.integration.produto;

import com.example.demo.auth.dto.LoginEnterDTO;
import com.example.demo.auth.tokens.TokenService;
import com.example.demo.categoria.Categoria;
import com.example.demo.categoria.CategoriaRepository;
import com.example.demo.produto.Produto;
import com.example.demo.produto.ProdutoRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UpdateProdutoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private TokenService tokenService;

    private static final String PRODUTO_URL = "/produtos";

    private Categoria categoriaAtiva;
    private Produto produtoExistente;
    private String token;

    @BeforeEach
    void setup() {
        categoriaAtiva = new Categoria();
        categoriaAtiva.setNome("comidas");
        categoriaAtiva.setDescricao("belas comidas");
        categoriaAtiva.setAtivo(true);
        categoriaRepository.save(categoriaAtiva);

        produtoExistente = new Produto();
        produtoExistente.setNome("Pizza");
        produtoExistente.setDescricao("Pizza calabresa");
        produtoExistente.setPreco(new BigDecimal("49.90"));
        produtoExistente.setAtivo(true);
        produtoExistente.setCategoria(categoriaAtiva);
        produtoRepository.save(produtoExistente);

        token = tokenService.generateToken(new LoginEnterDTO("avante@email.com", "123"));
    }

    @Test
    void ShouldUpdateProdutoSuccessfullyWhenFieldsOk() throws Exception {
        ProdutoRegisterDTO updateDTO = new ProdutoRegisterDTO(
                "Pizza de frango",
                "Pizza de frango com catupiry",
                new BigDecimal("50.00"),
                categoriaAtiva.getId()
        );

        String json = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(put(PRODUTO_URL + "/" + produtoExistente.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(produtoExistente.getId()))
                .andExpect(jsonPath("$.nome").value("pizza de frango"))
                .andExpect(jsonPath("$.descricao").value("pizza de frango com catupiry"))
                .andExpect(jsonPath("$.preco").value(50.00))
                .andExpect(jsonPath("$.categoriaId").value(categoriaAtiva.getId()));
    }

    @Test
    void ShouldThrowExceptionWhenProdutoNotFound() throws Exception {
        ProdutoRegisterDTO updateDTO = new ProdutoRegisterDTO(
                "Sanduíche",
                "Sanduíche natural",
                new BigDecimal("12.50"),
                categoriaAtiva.getId()
        );

        String json = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(put(PRODUTO_URL + "/100")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field").value("produto"))
                .andExpect(jsonPath("$.message").value("recurso nao encontrado"));
    }

    @Test
    void ShouldThrowExceptionWhenCategoriaNotFound() throws Exception {
        ProdutoRegisterDTO updateDTO = new ProdutoRegisterDTO(
                "Refrigerante",
                "Coca-Cola 2L",
                new BigDecimal("12.00"),
                100L
        );

        String json = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(put(PRODUTO_URL + "/" + produtoExistente.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field").value("categoria"))
                .andExpect(jsonPath("$.message").value("recurso nao encontrado"));
    }

    @Test
    void ShouldThrowExceptionWhenFieldsAreNullOrBlank() throws Exception {
        ProdutoRegisterDTO updateDTO = new ProdutoRegisterDTO(
                "",
                null,
                null,
                null
        );

        String json = objectMapper.writeValueAsString(updateDTO);

        mockMvc.perform(put(PRODUTO_URL + "/" + produtoExistente.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[?(@.field == 'nome')].message").value("O nome nao pode ser vazio"))
                .andExpect(jsonPath("$[?(@.field == 'descricao')].message").value("A descricao nao pode ser vazia"))
                .andExpect(jsonPath("$[?(@.field == 'preco')].message").value("o preco nao pode ser vazio"))
                .andExpect(jsonPath("$[?(@.field == 'categoriaId')].message").value("o id da categoria nao pode ser vazio"));
    }
}
