package com.example.demo.integration.produto;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class GetProdutoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    private static final String PRODUTO_URL = "/produtos";

    private Categoria categoria;
    private Produto produto1;
    private Produto produto2;

    @BeforeEach
    void setup() {
        categoria = new Categoria();
        categoria.setNome("comidas");
        categoria.setDescricao("belas comidas");
        categoria.setAtivo(true);
        categoriaRepository.save(categoria);

        produto1 = new Produto();
        produto1.setNome("hamburguer");
        produto1.setDescricao("hamburguer delicioso");
        produto1.setPreco(new BigDecimal("10.00"));
        produto1.setCategoria(categoria);
        produto1.setAtivo(true);
        produtoRepository.save(produto1);

        produto2 = new Produto();
        produto2.setNome("pizza");
        produto2.setDescricao("pizza deliciosa");
        produto2.setPreco(new BigDecimal("20.00"));
        produto2.setCategoria(categoria);
        produto2.setAtivo(true);
        produtoRepository.save(produto2);
    }

    @Test
    void ShouldReturnPagedProdutosSuccessfully_FirstPage() throws Exception {
        mockMvc.perform(get(PRODUTO_URL)
                        .param("page", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].nome").value("hamburguer"))
                .andExpect(jsonPath("$.content[0].descricao").value("hamburguer delicioso"))
                .andExpect(jsonPath("$.content[0].preco").value(10.00))
                .andExpect(jsonPath("$.page.size").value(1))
                .andExpect(jsonPath("$.page.number").value(0))
                .andExpect(jsonPath("$.page.totalElements").value(2))
                .andExpect(jsonPath("$.page.totalPages").value(2));
    }

    @Test
    void ShouldReturnPagedProdutosSuccessfully_SecondPage() throws Exception {
        mockMvc.perform(get(PRODUTO_URL)
                        .param("page", "1")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].nome").value("pizza"))
                .andExpect(jsonPath("$.page.number").value(1));
    }

    @Test
    void ShouldReturnEmptyContentWhenPageOutOfRange() throws Exception {
        mockMvc.perform(get(PRODUTO_URL)
                        .param("page", "5")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0))
                .andExpect(jsonPath("$.page.totalElements").value(2))
                .andExpect(jsonPath("$.page.totalPages").value(2));
    }

    @Test
    void ShouldReturnEmptyContentWhenCategoryIsSoftDeleted() throws Exception {
        categoria.setAtivo(false);
        categoriaRepository.save(categoria);

        mockMvc.perform(get(PRODUTO_URL)
                        .param("page", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(0));

    }
}

