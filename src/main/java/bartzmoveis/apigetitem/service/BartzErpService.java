package bartzmoveis.apigetitem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bartzmoveis.apigetitem.model.BartzErpDB;
import bartzmoveis.apigetitem.repository.BartzErpRepository;

@Service
public class BartzErpService {

    @Autowired
    private BartzErpRepository repository;

    @Transactional(readOnly = true)
    public List<BartzErpDB> listAll() {
        return repository.findAll();
    }

    @Transactional(readOnly=true)
    public Optional<BartzErpDB> findByCodeItem(String code){
        return repository.findByCodeItem(code);
    }

    @Transactional(readOnly = true)
    public Optional<BartzErpDB> findByDescriptionItem(String desc) {
        return repository.findByDescription(desc);
    }

    // --- NOVOS MÉTODOS DE BUSCA PARCIAL ---
    @Transactional(readOnly = true)
    public List<BartzErpDB> searchByDescription(String partialDesc) {
        // O método Containing já faz o %like%, mas podemos forçar UpperCase pra ignorar maiuscula/minuscula se o banco permitir
        return repository.findByDescriptionContaining(partialDesc);
    }

    @Transactional(readOnly = true)
    public List<BartzErpDB> searchByCode(String partialCode) {
        return repository.searchByPartCode(partialCode);
    }
}
