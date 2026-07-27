package bartzmoveis.apigetitem.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bartzmoveis.apigetitem.dto.ItemDTO;
import bartzmoveis.apigetitem.dto.ItemNodeDTO;
import bartzmoveis.apigetitem.service.ItemService;

// Esta classe é o controlador REST para a entidade Item, responsável por expor os 
// endpoints da API e lidar com as requisições HTTP
@RestController

// O @RequestMapping define a rota base para todos os endpoints deste
// controlador, ou seja,
// todos os endpoints começarão com /item
@RequestMapping("/itens")
public class ItemController {

    private ItemService service;

    private ItemController(ItemService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ItemDTO>> listAll() {
        List<ItemDTO> listItem = service.listAll();

        if (listItem.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listItem);
    }

    // URL: /item/search-code?code=10.01
    @GetMapping(value = "/search", params = "codigo")
    public ResponseEntity<List<ItemDTO>> searchByCode(@RequestParam("codigo") String query) {
        List<ItemDTO> results = service.findByCode(query);

        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    @GetMapping(value = "/search", params = "descricao")
    public ResponseEntity<List<ItemDTO>> searchByDescription(@RequestParam("descricao") String query) {
        List<ItemDTO> results = service.findByDescription(query);

        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    // URL: /itens/search?codigoBarras=7899000002410
    @GetMapping(value = "/search", params = "codigoBarras")
    public ResponseEntity<List<ItemDTO>> searchByBarcode(@RequestParam("codigoBarras") String query) {
        List<ItemDTO> results = service.findByBarcode(query);

        if (results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    // URL: /itens/estrutura?codigo=LR0020.0007
    // Retorna o item e, recursivamente, todos os itens filhos (árvore de
    // estrutura), equivalente à tela EPM019 do ERP.
    @GetMapping(value = "/estrutura", params = "codigo")
    public ResponseEntity<ItemNodeDTO> estrutura(@RequestParam("codigo") String codigo) {
        ItemNodeDTO raiz = service.getEstrutura(codigo);

        if (raiz == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(raiz);
    }
}