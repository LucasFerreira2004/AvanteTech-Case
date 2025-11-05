package com.example.demo.integration.categoria;

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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class GetCategoriaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private static final String CATEGORIA_URL = "/categorias";
    private Categoria cat1;
    private Categoria cat2;

    @BeforeEach
    void setup() {
        cat1 = new Categoria();
        cat1.setNome("comidas");
        cat1.setDescricao("belas comidas");
        cat1.setAtivo(true);

        cat2 = new Categoria();
        cat2.setNome("limpeza");
        cat2.setDescricao("produtos de limpeza");
        cat2.setAtivo(true);

        categoriaRepository.save(cat1);
        categoriaRepository.save(cat2);
    }

    @Test
    void ShouldReturnPagedCategoriasSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(CATEGORIA_URL)
                        .param("page", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))

                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].nome").value("comidas"))
                .andExpect(jsonPath("$.content[0].descricao").value("belas comidas"));
    }

    @Test
    void ShouldReturnSecondPageSuccessfully() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(CATEGORIA_URL)
                        .param("page", "1")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))

                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].nome").value("limpeza"))
                .andExpect(jsonPath("$.content[0].descricao").value("produtos de limpeza"))
                .andExpect(jsonPath("$.page.number").value(1));
    }

    @Test
    void ShoudNotReturnCategoriaWhenCategoriaIsSoftDeleted() throws Exception {
        cat1.setAtivo(false);
        categoriaRepository.save(cat1);

        mockMvc.perform(MockMvcRequestBuilders.get(CATEGORIA_URL)
                        .param("page", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].nome").value("limpeza"))
                .andExpect(jsonPath("$.content[0].descricao").value("produtos de limpeza"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.page.number").value(0));
    }
}
