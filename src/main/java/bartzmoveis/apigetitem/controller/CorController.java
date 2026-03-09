package bartzmoveis.apigetitem.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bartzmoveis.apigetitem.model.Cor;
import bartzmoveis.apigetitem.service.CorService;

@RestController
@RequestMapping("/api/cor")
@CrossOrigin(origins = {"http://192.168.1.10:50000", "http://localhost:5173", "file://"})
public class CorController {
    
    private CorService corService;
    public CorController(CorService corService){
        this.corService = corService;
    }

    @GetMapping
    public ResponseEntity<Page<Cor>> listAll(Pageable pageable){
        return ResponseEntity.ok(corService.listAll(pageable));
    }

    @GetMapping("/find-by-sigla")
    public ResponseEntity<?> findBySiglaCor(@RequestParam("q") String siglaCor){
        Optional<Cor> corOpt = corService.findBySiglaCor(siglaCor);
        if(corOpt.isPresent()){
            return ResponseEntity.ok(corOpt.get());
        }
        return ResponseEntity.status(404).body("Cor não encontrada");
    }

    @GetMapping("/find-by-descricao")
    public ResponseEntity<?> findByDescricao(@RequestParam("q") String descricao){
        Optional<Cor> corOpt = corService.findByDescricao(descricao);
        if(corOpt.isPresent()){
            return ResponseEntity.ok(corOpt.get());
        }
        return ResponseEntity.status(404).body("Cor não encontrada");
    }

    @GetMapping("/search-sigla")
    public ResponseEntity<List<Cor>> searchBySiglaCor(@RequestParam("q") String query){
        List<Cor> results = corService.searchBySiglaCor(query);
        if(results.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search-descricao")
    public ResponseEntity<List<Cor>> searchByDescricao(@RequestParam("q") String query){
        List<Cor> results = corService.searchByDescricao(query);
        if(results.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }
}
