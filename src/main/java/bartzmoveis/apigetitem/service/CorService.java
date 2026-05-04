package bartzmoveis.apigetitem.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import bartzmoveis.apigetitem.dto.CorDTO;

@Service
public class CorService {
    private JdbcTemplate jdbcTemplate;
    public CorService(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    public List<CorDTO> listAll(){
        String sql = "SELECT SIGLA_COR, DESCRICAO FROM SCHEMA.COR";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CorDTO dto = new CorDTO();
            dto.setSiglaCor(rs.getString("SIGLA_COR"));
            dto.setDescricao(rs.getString("DESCRICAO"));
            return dto;
        });
    }

    @Transactional(readOnly = true)
    public List<CorDTO> findBySiglaCor(String siglaCor){
        String sql = "SELECT SIGLA_COR, DESCRICAO FROM SCHEMA.COR " + "WHERE UPPER(SIGLA_COR) LIKE UPPER(?)";
        
        String formattedSql = "%" + siglaCor + "%";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CorDTO dto = new CorDTO();
            dto.setSiglaCor(rs.getString("SIGLA_COR"));
            dto.setDescricao(rs.getString("DESCRICAO"));
            return dto;
        }, formattedSql);
    }

    @Transactional(readOnly = true)
    public List<CorDTO> findByDescricao(String descricao){
        String sql = "SELECT SIGLA_COR, DESCRICAO FROM SCHEMA.COR " + "WHERE UPPER(DESCRICAO) LIKE UPPER(?)";

        String formattedSql = "%" + descricao + "%";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CorDTO dto = new CorDTO();
            dto.setSiglaCor(rs.getString("SIGLA_COR"));
            dto.setDescricao(rs.getString("DESCRICAO"));
            return dto;
        }, formattedSql);
    }
}
