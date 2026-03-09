package bartzmoveis.apigetitem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import bartzmoveis.apigetitem.model.Cor;
import bartzmoveis.apigetitem.repository.CorRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CorService {
    private CorRepository corRepository;
    public CorService(CorRepository corRepository){
        this.corRepository = corRepository;
    }

    @Transactional(readOnly = true)
    public Page<Cor> listAll(Pageable pageable){
        return corRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Cor> findBySiglaCor(String siglaCor){
        return corRepository.findBySiglaCor(siglaCor);
    }

    @Transactional(readOnly = true)
    public Optional<Cor> findByDescricao(String descricao){
        return corRepository.findByDescricao(descricao);
    }

    @Transactional(readOnly = true)
    public List<Cor> searchBySiglaCor(String siglaCor){
        return corRepository.findBySiglaCorContaining(siglaCor);
    }

    @Transactional(readOnly = true)
    public List<Cor> searchByDescricao(String descricao){
        return corRepository.findByDescricaoContaining(descricao);
    }
}
