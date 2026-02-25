package bartzmoveis.apigetitem.controller;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import bartzmoveis.apigetitem.model.BartzErpDB;
import bartzmoveis.apigetitem.repository.BartzErpRepository;
import bartzmoveis.apigetitem.service.BartzErpService;
import bartzmoveis.apigetitem.config.ApiKeyProperties;

@WebMvcTest(BartzErpController.class)
@AutoConfigureMockMvc(addFilters = false) // Desabilita o Spring Security
public class BartzErpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BartzErpService service;

    @MockBean
    private BartzErpRepository repository;

    // Fazendo MockBean da configuração de segurança para evitar
    // NoSuchBeanDefinitionException
    @MockBean
    private ApiKeyProperties apiKeyProperties;

    private BartzErpDB mockItem;

    @BeforeEach
    void setUp() {
        mockItem = mock(BartzErpDB.class);
        org.mockito.Mockito.lenient().when(mockItem.getCodeItem()).thenReturn("10.01");
        org.mockito.Mockito.lenient().when(mockItem.getDescription()).thenReturn("Armario");
    }

    @Test
    void listAll_ShouldReturnPaginatedItems() throws Exception {
        Page<BartzErpDB> pagedResponse = new PageImpl<>(Arrays.asList(mockItem));
        when(repository.findAll(any(Pageable.class))).thenReturn(pagedResponse);

        mockMvc.perform(get("/api/erp")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].codeItem", is("10.01")));
    }

    @Test
    void findByCodeItem_WhenExists_ShouldReturn200() throws Exception {
        when(service.findByCodeItem("10.01")).thenReturn(Optional.of(mockItem));

        mockMvc.perform(get("/api/erp/find-by-code").param("q", "10.01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codeItem", is("10.01")));
    }

    @Test
    void findByCodeItem_WhenNotExists_ShouldReturn404() throws Exception {
        when(service.findByCodeItem("99.99")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/erp/find-by-code").param("q", "99.99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchByCode_WhenResultsExist_ShouldReturn200() throws Exception {
        when(service.searchByCode("10")).thenReturn(Arrays.asList(mockItem));

        mockMvc.perform(get("/api/erp/search-code").param("q", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].codeItem", is("10.01")));
    }

    @Test
    void searchByCode_WhenNoResults_ShouldReturn204() throws Exception {
        when(service.searchByCode("88")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/erp/search-code").param("q", "88"))
                .andExpect(status().isNoContent());
    }
}
