package bartzmoveis.apigetitem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bartzmoveis.apigetitem.model.BartzErpDB;
import bartzmoveis.apigetitem.repository.BartzErpRepository;
import bartzmoveis.apigetitem.service.BartzErpService;

// Esta classe é o controlador REST para a entidade BartzErpDB, responsável por expor os 
// endpoints da API e lidar com as requisições HTTP
@RestController

// O @RequestMapping define a rota base para todos os endpoints deste controlador, ou seja,
// todos os endpoints começarão com /api/erp
@RequestMapping("/api/erp")

// O @CrossOrigin é usado para permitir requisições de origens específicas, como o frontend
// rodando em localhost ou um arquivo local
@CrossOrigin(origins = {"http://192.168.1.10:50000", "http://localhost:5173", "file://"})
public class BartzErpController {

    @Autowired
    private BartzErpService service;
    @Autowired
    private BartzErpRepository repository;

    // O método listAll() é mapeado para o endpoint GET /api/erp, e retorna uma página de itens do banco
    // O Pageable é um objeto que contém informações sobre a página solicitada (número, tamanho, ordenação), 
    // e o Spring Data JPA cuida de paginar os resultados automaticamente
    @GetMapping
    public Page<BartzErpDB> listAll(Pageable pageable) {
        return repository.findAll(pageable);
    }


    // 2. Buscar por código usando GET (Query Parameter)
    // A URL será: /api/erp/find-by-code?code=12345
    @GetMapping("/find-by-code") 
    public ResponseEntity<?> findByCodeItem(@RequestParam("code") String code) {
        
        Optional<BartzErpDB> erpOpt = service.findByCodeItem(code);

        if (erpOpt.isPresent()) {
            return ResponseEntity.ok(erpOpt.get()); 
        }
        return ResponseEntity.status(404).body("Item não encontrado");
    }

    // 3. Buscar por descrição usando GET
    // A URL será: /api/erp/find-by-description?desc=armario
    @GetMapping("/find-by-description")
    public ResponseEntity<?> findByDescriptionItem(@RequestParam("desc") String desc) {
        
        Optional<BartzErpDB> erpOpt = service.findByDescriptionItem(desc);

        if (erpOpt.isPresent()) {
            return ResponseEntity.ok(erpOpt.get()); 
        }
        return ResponseEntity.status(404).body("Item não encontrado");
    }

    
    // NOVO: Busca parcial por código
    // URL: /api/erp/search-code?q=10.01
    @GetMapping("/search-code")
    public ResponseEntity<List<BartzErpDB>> searchByCode(@RequestParam("q") String query) {
        List<BartzErpDB> results = service.searchByCode(query);
        
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retorna 204 se não achar nada
        }
        return ResponseEntity.ok(results);
    }


    // NOVO: Busca parcial por descrição
    // URL: /api/erp/search-desc?q=branco
    @GetMapping("/search-desc")
    public ResponseEntity<List<BartzErpDB>> searchByDescription(@RequestParam("q") String query) {
        List<BartzErpDB> results = service.searchByDescription(query);
        
        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }
}