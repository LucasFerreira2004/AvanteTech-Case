package com.example.demo.integration.produto;

import com.example.demo.auth.dto.LoginEnterDTO;
import com.example.demo.auth.tokens.TokenService;
import com.example.demo.categoria.Categoria;
import com.example.demo.categoria.CategoriaRepository;
import com.example.demo.produto.Produto;
import com.example.demo.produto.ProdutoRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class DeleteProdutoIntegrationTest {

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
    private LoginEnterDTO loginEnterDTO;

    @BeforeEach
    void setup() {
        loginEnterDTO = new LoginEnterDTO("avante@gmail.com", "123");
        token = tokenService.generateToken(loginEnterDTO);

        categoriaAtiva = new Categoria();
        categoriaAtiva.setNome("bebidas");
        categoriaAtiva.setDescricao("bebidas refrescantes");
        categoriaAtiva.setAtivo(true);
        categoriaRepository.save(categoriaAtiva);

        produtoExistente = new Produto();
        produtoExistente.setNome("suco");
        produtoExistente.setDescricao("suco de laranja natural");
        produtoExistente.setPreco(new BigDecimal("10.00"));
        produtoExistente.setCategoria(categoriaAtiva);
        produtoExistente.setAtivo(true);
        produtoRepository.save(produtoExistente);
    }

    @Test
    void ShouldSoftDeleteProdutoSuccessfully() throws Exception {
        mockMvc.perform(delete(PRODUTO_URL + "/" + produtoExistente.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void ShouldThrowExceptionWhenProdutoNotFound() throws Exception {
        mockMvc.perform(delete(PRODUTO_URL + "/100")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field").value("produto"))
                .andExpect(jsonPath("$.message").value("recurso nao encontrado"));
    }

    @Test
    void ShouldThrowExceptionWhenProdutoAlreadyInactive() throws Exception {
        produtoExistente.setAtivo(false);
        produtoRepository.save(produtoExistente);

        mockMvc.perform(delete(PRODUTO_URL + "/" + produtoExistente.getId())
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field").value("produto"))
                .andExpect(jsonPath("$.message").value("recurso nao encontrado"));
    }
}
