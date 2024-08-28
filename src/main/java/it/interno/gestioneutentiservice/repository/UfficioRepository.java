package it.interno.gestioneutentiservice.repository;

import it.interno.gestioneutentiservice.entity.Ufficio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UfficioRepository extends JpaRepository<Ufficio, String> {

    @Query(value = "SELECT sul.* " +
            "FROM SSD_SECURITY.SEC_COMANDANTE_UFFICI scu INNER JOIN SSD_SECURITY.SEC_UFFICIO_LEVEL sul ON scu.UFF_SEGN = sul.COD_UFF " +
            "WHERE TRIM(scu.UTE_CMD) = TRIM(?1) AND scu.DATA_CAN IS NULL AND sul.DATA_CAN IS NULL ", nativeQuery = true)
    List<Ufficio> getUfficiByComandante(String codiceUtente);

}
