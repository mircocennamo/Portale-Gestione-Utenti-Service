package it.interno.gestioneutentiservice.repository;

import it.interno.gestioneutentiservice.entity.GroupMembers;
import it.interno.gestioneutentiservice.entity.pk.GroupMembersKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupMembersRepository extends JpaRepository<GroupMembers, GroupMembersKey> {

    @Query("FROM GroupMembers g WHERE TRIM(g.nomeUtente) = TRIM(?1) AND g.dataCancellazione IS NULL")
    List<GroupMembers> getAllByUtente(String codiceUtente);

    @Query(value = "SELECT IS_RUOLO_APPLICATIVO_ASSEGNABILE(:codiceUtente, :idApp, :codiceRuolo) FROM DUAL", nativeQuery = true)
    Integer isRuoloApplicativoAssegnabile(String codiceUtente, String idApp, String codiceRuolo);
}
