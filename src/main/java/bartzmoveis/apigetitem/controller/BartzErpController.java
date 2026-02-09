package bartzmoveis.apigetitem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import bartzmoveis.apigetitem.model.BartzErpDB;
import bartzmoveis.apigetitem.service.BartzErpService;

@RestController
@RequestMapping("/api/erp")
@CrossOrigin(origins = "*") 
public class BartzErpController {

    @Autowired
    private BartzErpService service;

    // 1. Listar tudo continua igual
    @GetMapping
    public List<BartzErpDB> listAll() {
        return service.listAll();
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
}