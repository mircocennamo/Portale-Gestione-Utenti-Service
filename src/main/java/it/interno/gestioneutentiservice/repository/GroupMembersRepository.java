package it.interno.gestioneutentiservice.repository;

import it.interno.gestioneutentiservice.entity.GroupMembers;
import it.interno.gestioneutentiservice.entity.pk.GroupMembersKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupMembersRepository extends JpaRepository<GroupMembers, GroupMembersKey> {

    @Query("FROM GroupMembers g WHERE TRIM(g.nomeUtente) = TRIM(?1) AND g.dataCancellazione IS NULL")
    List<GroupMembers> getAllByUtente(String codiceUtente);

    @Query(value = "SELECT SSD_SECURITY.IS_RUOLO_APPLICATIVO_ASSEGNABILE(:codiceUtente, :idApp, :codiceRuolo) FROM DUAL", nativeQuery = true)
    Integer isRuoloApplicativoAssegnabile(String codiceUtente, String idApp, String codiceRuolo);

    @Modifying
    @Query(value = "INSERT INTO SSD_SECURITY.SEC_GROUPMEMBER_OIM " +
            "(USERNAME, GROUPNAME, DATA_RICHIESTA, STATO) " +
            "VALUES ( TRIM(?1), TRIM(?2), SYSDATE, 'DA RIMUOVERE')", nativeQuery = true)
    void deleteGroupMembersOimByUtenteERuolo(String codiceUtente, String nomeRuolo);

    @Modifying
    @Query(value = "DELETE FROM SSD_SECURITY.SEC_GROUPMEMBER_OIM " +
            "WHERE TRIM(USERNAME) = TRIM(?1) " +
            "AND TRIM(GROUPNAME) = TRIM(?2) " +
            "AND STATO IN ('DA ASSEGNARE','DA RIMUOVERE' )", nativeQuery = true)
    void deleteGroupMembersOimInStagingByUtenteERuolo(String codiceUtente, String nomeRuolo);
}
