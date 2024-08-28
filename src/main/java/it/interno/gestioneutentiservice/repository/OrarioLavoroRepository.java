package it.interno.gestioneutentiservice.repository;

import it.interno.gestioneutentiservice.entity.OrarioLavoro;
import it.interno.gestioneutentiservice.entity.pk.OrarioLavoroPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrarioLavoroRepository extends JpaRepository<OrarioLavoro, OrarioLavoroPK> {

    @Query("FROM OrarioLavoro o WHERE TRIM(codiceUtente) = TRIM(?1) AND dataCancellazione IS NULL")
    OrarioLavoro getOrarioLavoroByUtente(String codiceUtente);

}
