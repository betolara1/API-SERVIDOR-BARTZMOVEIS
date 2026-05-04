package bartzmoveis.apigetitem.service;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bartzmoveis.apigetitem.dto.ItemDTO;

// Esta classe é a camada de serviço para a entidade Item, responsável por 
// implementar a lógica de negócios
@Service
public class ItemService {

    // O repositório é injetado para que possamos acessar os dados do banco e
    // realizar as operações necessárias
    private final JdbcTemplate jdbcTemplate;
    public ItemService (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ItemDTO> listAll(){
        String sql = "SELECT ITEM, DESCRICAO, REF_COMERCIAL FROM SCHEMA.ITEM";
        
        //O RowMapper transforma cada linha do banco em um objeto DTO
        //Cada campo da tabela é mapeado para um campo do DTO
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemDTO dto = new ItemDTO();
            dto.setCodeItem(rs.getString("ITEM"));
            dto.setDescription(rs.getString("DESCRICAO"));
            dto.setRefComercial(rs.getString("REF_COMERCIAL"));
            return dto;
        });
    }


    @Transactional(readOnly = true)
    public List<ItemDTO> findByCode(String code) {
        //Usa-se UPPER para a busca não diferenciar maiúsculas de minúsculas
        //Usa-se LIKE ? para que ele retorne qualquer valor que contenha o código
        String sql = "SELECT ITEM, DESCRICAO, REF_COMERCIAL FROM SCHEMA.ITEM " + "WHERE UPPER(ITEM) LIKE UPPER(?)";

        String formattedSql = "%" + code + "%";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemDTO dto = new ItemDTO();
            dto.setCodeItem(rs.getString("ITEM"));
            dto.setDescription(rs.getString("DESCRICAO"));
            dto.setRefComercial(rs.getString("REF_COMERCIAL"));
            return dto;
        }, formattedSql); // O formattedSql substitui o ? no SQL
    }

    @Transactional(readOnly = true)
    public List<ItemDTO> findByDescription(String desc) {
        String sql = "SELECT ITEM, DESCRICAO, REF_COMERCIAL FROM SCHEMA.ITEM " + "WHERE UPPER(DESCRICAO) LIKE UPPER(?)";

        String formattedSql = "%" + desc + "%";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ItemDTO dto = new ItemDTO();
            dto.setCodeItem(rs.getString("ITEM"));
            dto.setDescription(rs.getString("DESCRICAO"));
            dto.setRefComercial(rs.getString("REF_COMERCIAL"));
            return dto;
        }, formattedSql);
    }
}
