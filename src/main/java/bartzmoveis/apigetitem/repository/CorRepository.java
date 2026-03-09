package bartzmoveis.apigetitem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bartzmoveis.apigetitem.model.Cor;

@Repository
public interface CorRepository extends JpaRepository<Cor, String> {

    // Métodos personalizados para buscar por código e descrição, usando Optional para lidar 
    // com casos de não encontrado
    Page<Cor> findAll(Pageable pageable);

    // Busca por sigla
    Optional<Cor> findBySiglaCor(String siglaCor);

    // Busca por descrição
    Optional<Cor> findByDescricao(String descricao);

    // Busca parcial por sigla
    List<Cor> findBySiglaCorContaining(String siglaCor);

    // Busca parcial por descrição
    List<Cor> findByDescricaoContaining(String descricao);
}
