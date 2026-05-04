package bartzmoveis.apigetitem.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import bartzmoveis.apigetitem.service.CorService;
import bartzmoveis.apigetitem.config.ApiKeyProperties;
import bartzmoveis.apigetitem.dto.CorDTO;

@WebMvcTest(CorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CorService service;

    @MockitoBean
    private ApiKeyProperties apiKeyProperties;

    private CorDTO mockCor;

    @BeforeEach
    void setUp() {
        mockCor = new CorDTO("BR", "Branco");
    }

    @Test
    void listAll_ShouldReturnColors() throws Exception {
        when(service.listAll()).thenReturn(Arrays.asList(mockCor));

        mockMvc.perform(get("/cor"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].siglaCor", is("BR")));
    }

    @Test
    void listAll_WhenEmpty_ShouldReturn204() throws Exception {
        when(service.listAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cor"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findBySiglaCor_WhenExists_ShouldReturn200() throws Exception {
        when(service.findBySiglaCor("BR")).thenReturn(Arrays.asList(mockCor));

        mockMvc.perform(get("/cor/cor").param("code", "BR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].siglaCor", is("BR")));
    }

    @Test
    void findBySiglaCor_WhenNotExists_ShouldReturn204() throws Exception {
        when(service.findBySiglaCor("XX")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/cor/cor").param("code", "XX"))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchByDescricao_WhenExists_ShouldReturn200() throws Exception {
        when(service.findByDescricao("Branco")).thenReturn(Arrays.asList(mockCor));

        mockMvc.perform(get("/cor/descricao").param("desc", "Branco"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].descricao", is("Branco")));
    }
}
