package bartzmoveis.apigetitem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bartzmoveis.apigetitem.model.Item;

// Esta interface é o repositório JPA para a entidade Item, responsável por acessar 
// o banco de dados e realizar as operações de CRUD e consultas personalizadas
@Repository
public interface ItemRepository extends JpaRepository<Item, String> {

    // O método findAll(Pageable pageable) é herdado de JpaRepository, mas podemos
    // sobrescrevê-lo
    // para garantir que ele retorne uma Page em vez de List, facilitando a
    // paginação
    @Override
    Page<Item> findAll(Pageable pageable);

    // Métodos personalizados para buscar por código e descrição, usando Optional
    // para lidar
    // com casos de não encontrado
    Optional<Item> findByCodeItem(String code);

    Optional<Item> findByDescription(String description);

    // --- NOVOS MÉTODOS DE BUSCA PARCIAL ---
    // NOVO: Busca parcial por Descrição (O Spring entende "Containing" como LIKE
    // %texto%)
    // Ex: "branco" acha "Armario Branco", "Branco Fosco", etc.
    List<Item> findByDescriptionContaining(String text);

    // NOVO: Busca parcial por Código
    // Como sua variável é 'code_item', usamos @Query para evitar confusão do Spring
    @Query("SELECT i FROM Item i WHERE i.codeItem LIKE %:code%")
    List<Item> searchByPartCode(@Param("code") String code);
}
