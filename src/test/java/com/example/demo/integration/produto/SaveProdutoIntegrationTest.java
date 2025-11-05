package com.example.demo.integration.produto;

import com.example.demo.categoria.Categoria;
import com.example.demo.categoria.CategoriaRepository;
import com.example.demo.categoria.dtos.CategoriaRegisterDTO;
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

    private static final String PRODUTO_URL = "/produtos";

    private Categoria categoriaAtiva;

    @BeforeEach
    void setup() {
        categoriaAtiva = new Categoria();
        categoriaAtiva.setNome("comidas");
        categoriaAtiva.setDescricao("belas comidas");
        categoriaAtiva.setAtivo(true);
        categoriaRepository.save(categoriaAtiva);
    }

    @Test
    void ShouldSaveProdutoSuccessfullyWhenFieldsOk() throws Exception {
        ProdutoRegisterDTO registerDTO = new ProdutoRegisterDTO(
                "Hambúrguer",
                "Delicioso hambúrguer",
                new BigDecimal("29.90"),
                categoriaAtiva.getId()
        );
        String json = objectMapper.writeValueAsString(registerDTO);

        mockMvc.perform(post(PRODUTO_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // ✅ Verifica se os campos retornam corretamente
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("hambúrguer"))
                .andExpect(jsonPath("$.descricao").value("delicioso hambúrguer artesanal"))
                .andExpect(jsonPath("$.preco").value(29.90))
                .andExpect(jsonPath("$.categoriaId").value(categoriaAtiva.getId()));
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // ✅ Valida mensagens de erro esperadas (a ordem pode variar)
                .andExpect(jsonPath("$[?(@.field == 'nome')].message").value("O nome nao pode ser vazio"))
                .andExpect(jsonPath("$[?(@.field == 'descricao')].message").value("A descricao nao pode ser vazia"))
                .andExpect(jsonPath("$[?(@.field == 'preco')].message").value("o preco nao pode ser vazio"))
                .andExpect(jsonPath("$[?(@.field == 'categoriaId')].message").value("o id da categoria nao pode ser vazio"));
    }

    @Test
    void ShouldThrowExceptionWhenCategoriaNotFound() throws Exception {
        ProdutoRegisterDTO registerDTO = new ProdutoRegisterDTO(
                "Pizza",
                "Pizza calabresa",
                new BigDecimal("59.90"),
                999L // id inexistente
        );

        String json = objectMapper.writeValueAsString(registerDTO);

        mockMvc.perform(post(PRODUTO_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field").value("categoria"))
                .andExpect(jsonPath("$.message").value("recurso nao encontrado"));
    }

    @Test
    void ShouldThrowExceptionWhenCategoriaIsInactive() throws Exception {
        // cria uma categoria inativa
        Categoria categoriaInativa = new Categoria();
        categoriaInativa.setNome("limpeza");
        categoriaInativa.setDescricao("produtos de limpeza");
        categoriaInativa.setAtivo(false);
        categoriaRepository.save(categoriaInativa);

        ProdutoRegisterDTO registerDTO = new ProdutoRegisterDTO(
                "Detergente",
                "Detergente neutro 500ml",
                new BigDecimal("5.50"),
                categoriaInativa.getId()
        );

        String json = objectMapper.writeValueAsString(registerDTO);

        mockMvc.perform(post(PRODUTO_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.field").value("categoria"))
                .andExpect(jsonPath("$.message").value("categoria inativa"));
    }
}