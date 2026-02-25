package bartzmoveis.apigetitem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import bartzmoveis.apigetitem.model.BartzErpDB;
import bartzmoveis.apigetitem.repository.BartzErpRepository;

@ExtendWith(MockitoExtension.class)
public class BartzErpServiceTest {

    @Mock
    private BartzErpRepository repository;

    @InjectMocks
    private BartzErpService service;

    private BartzErpDB mockItem;

    @BeforeEach
    void setUp() {
        mockItem = mock(BartzErpDB.class);
        org.mockito.Mockito.lenient().when(mockItem.getCodeItem()).thenReturn("10.01");
        org.mockito.Mockito.lenient().when(mockItem.getDescription()).thenReturn("Armario Branco");
    }

    @Test
    void listAll_ShouldReturnAllItems() {
        when(repository.findAll()).thenReturn(Arrays.asList(mockItem));
        
        List<BartzErpDB> result = service.listAll();
        
        assertEquals(1, result.size());
        assertEquals("10.01", result.get(0).getCodeItem());
        verify(repository, times(1)).findAll();
    }

    @Test
    void findByCodeItem_WhenExists_ShouldReturnItem() {
        when(repository.findByCodeItem("10.01")).thenReturn(Optional.of(mockItem));
        
        Optional<BartzErpDB> result = service.findByCodeItem("10.01");
        
        assertTrue(result.isPresent());
        assertEquals("10.01", result.get().getCodeItem());
    }

    @Test
    void findByCodeItem_WhenNotExists_ShouldReturnEmpty() {
        when(repository.findByCodeItem("99.99")).thenReturn(Optional.empty());
        
        Optional<BartzErpDB> result = service.findByCodeItem("99.99");
        
        assertTrue(result.isEmpty());
    }

    @Test
    void searchByDescription_ShouldReturnMatchingItems() {
        when(repository.findByDescriptionContaining("Branco")).thenReturn(Arrays.asList(mockItem));
        
        List<BartzErpDB> result = service.searchByDescription("Branco");
        
        assertEquals(1, result.size());
        assertEquals("Armario Branco", result.get(0).getDescription());
    }

    @Test
    void searchByCode_ShouldReturnMatchingItems() {
        when(repository.searchByPartCode("10")).thenReturn(Arrays.asList(mockItem));
        
        List<BartzErpDB> result = service.searchByCode("10");
        
        assertEquals(1, result.size());
        assertEquals("10.01", result.get(0).getCodeItem());
    }
}
