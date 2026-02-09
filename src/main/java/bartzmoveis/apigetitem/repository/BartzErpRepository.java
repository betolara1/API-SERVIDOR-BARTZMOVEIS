package bartzmoveis.apigetitem.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import bartzmoveis.apigetitem.model.BartzErpDB;

@Repository
public interface BartzErpRepository extends JpaRepository<BartzErpDB, String> {
    Optional<BartzErpDB> findByCodeItem(String code);
    Optional<BartzErpDB> findByDescription(String description);

    // NOVO: Busca parcial por Descrição (O Spring entende "Containing" como LIKE %texto%)
    // Ex: "branco" acha "Armario Branco", "Branco Fosco", etc.
    List<BartzErpDB> findByDescriptionContaining(String text);

    // NOVO: Busca parcial por Código
    // Como sua variável é 'code_item', usamos @Query para evitar confusão do Spring
    @Query("SELECT i FROM BartzErpDB i WHERE i.codeItem LIKE %:code%")
    List<BartzErpDB> searchByPartCode(@Param("code") String code);
}
