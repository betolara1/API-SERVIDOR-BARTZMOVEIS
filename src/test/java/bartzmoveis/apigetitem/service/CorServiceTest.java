package bartzmoveis.apigetitem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import bartzmoveis.apigetitem.dto.CorDTO;

@ExtendWith(MockitoExtension.class)
public class CorServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private CorService service;

    private CorDTO mockCor;

    @BeforeEach
    void setUp() {
        mockCor = new CorDTO("BR", "Branco");
    }

    @Test
    @SuppressWarnings("unchecked")
    void listAll_ShouldReturnAllColors() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
            .thenReturn(Arrays.asList(mockCor));
        
        List<CorDTO> result = service.listAll();
        
        assertEquals(1, result.size());
        assertEquals("BR", result.get(0).getSiglaCor());
        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    @SuppressWarnings("unchecked")
    void findBySiglaCor_ShouldReturnMatchingColors() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq("%BR%")))
            .thenReturn(Arrays.asList(mockCor));
        
        List<CorDTO> result = service.findBySiglaCor("BR");
        
        assertEquals(1, result.size());
        assertEquals("BR", result.get(0).getSiglaCor());
    }

    @Test
    @SuppressWarnings("unchecked")
    void findByDescricao_ShouldReturnMatchingColors() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq("%Branco%")))
            .thenReturn(Arrays.asList(mockCor));
        
        List<CorDTO> result = service.findByDescricao("Branco");
        
        assertEquals(1, result.size());
        assertEquals("Branco", result.get(0).getDescricao());
    }

    @Test
    @SuppressWarnings("unchecked")
    void findBySiglaCor_WhenNoResults_ShouldReturnEmptyList() {
        when(jdbcTemplate.query(anyString(), any(RowMapper.class), anyString()))
            .thenReturn(Collections.emptyList());
        
        List<CorDTO> result = service.findBySiglaCor("XX");
        
        assertTrue(result.isEmpty());
    }
}
