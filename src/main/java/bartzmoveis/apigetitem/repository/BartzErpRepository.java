package bartzmoveis.apigetitem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import bartzmoveis.apigetitem.model.BartzErpDB;

@Repository
public interface BartzErpRepository extends JpaRepository<BartzErpDB, String> {
    Optional<BartzErpDB> findByCodeItem(String code);
    Optional<BartzErpDB> findByDescription(String description);
}
