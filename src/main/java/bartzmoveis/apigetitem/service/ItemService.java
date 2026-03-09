package bartzmoveis.apigetitem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bartzmoveis.apigetitem.model.Item;
import bartzmoveis.apigetitem.repository.ItemRepository;

// Esta classe é a camada de serviço para a entidade Item, responsável por 
// implementar a lógica de negócios
@Service
public class ItemService {

    // O repositório é injetado para que possamos acessar os dados do banco e
    // realizar as operações necessárias
    @Autowired
    private ItemRepository repository;

    // O método listAll() retorna uma lista de todos os itens do banco, usando o
    // método findAll() do repositório
    // O @Transactional(readOnly = true) é usado para otimizar a consulta, indicando
    // que não haverá alterações no banco

    @Transactional(readOnly = true)
    public List<Item> listAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Item> findByCodeItem(String code) {
        return repository.findByCodeItem(code);
    }

    @Transactional(readOnly = true)
    public Optional<Item> findByDescriptionItem(String desc) {
        return repository.findByDescription(desc);
    }

    // --- NOVOS MÉTODOS DE BUSCA PARCIAL ---
    @Transactional(readOnly = true)
    public List<Item> searchByDescription(String partialDesc) {
        // O método Containing já faz o %like%, mas podemos forçar UpperCase pra ignorar
        // maiuscula/minuscula se o banco permitir
        return repository.findByDescriptionContaining(partialDesc);
    }

    @Transactional(readOnly = true)
    public List<Item> searchByCode(String partialCode) {
        return repository.searchByPartCode(partialCode);
    }
}
