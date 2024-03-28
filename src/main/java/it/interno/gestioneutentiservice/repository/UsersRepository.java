package it.interno.gestioneutentiservice.repository;

import it.interno.gestioneutentiservice.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.Tuple;
import java.util.List;

public interface UsersRepository extends JpaRepository<Users, String> {

    @Query(value = "SELECT DOMINIO FROM SEC_DOMINIO_EMAIL s WHERE s.DOMINIO LIKE ?1% AND s.PRIMARY_EMAIL = ?2", nativeQuery = true)
    List<String> getDominioEmailLike(String dominio, Integer emailPrimaria);

    @Modifying
    @Query(value = "UPDATE SEC_OIM_USER_STAGING SET STATO_ACCOUNT = 3 WHERE TRIM(USERNAME) = TRIM(?1)", nativeQuery = true)
    void resetPassword(String codiceUtente);

    @Modifying
    @Query(value = "INSERT INTO LOG_CAMBIO_PASSWORD (G_MEMBER, G_MEMBER_FP, DATE_INS) " +
            "VALUES (?1, ?2, systimestamp)", nativeQuery = true)
    void logResetPassword(String utente, String utenteOperatore);

    @Query(value = "SELECT DISTINCT u.* " +
            "FROM USERS u LEFT OUTER JOIN SEC_COMANDANTE_UFFICI scu ON TRIM(u.G_MEMBER) = TRIM(scu.UTE_CMD) " +
            "WHERE TRIM(u.G_MEMBER) in(SELECT TRIM(USERNAME) FROM V_QUERYUTENZEABILITATE vq) " +
            "AND u.DATA_CAN IS NULL " +
            "AND (?1 IS NULL OR u.NOME LIKE ?1% OR u.COGNOME LIKE ?1% OR u.G_MEMBER LIKE ?1%) " +
            "AND (?2 IS NULL OR u.FORZA_POLIZIA = ?2) " +
            "AND (?3 IS NULL OR u.G_UFFICIO = ?3) " +
            "AND (?4 = 0 OR (?4 = 1 AND scu.UTE_CMD IS NOT NULL) OR (?4 = 2 AND scu.UTE_CMD IS NULL)) " +
            "AND ( " +
            "    ( " +
            "       ?6 = 'R_FP_LOCALE' " +
            "       AND EXISTS (SELECT * FROM USERS_ORARIO_LAVORO uol WHERE TRIM(uol.G_MEMBER) = TRIM(u.G_MEMBER) AND uol.DATA_CAN IS NULL) " +
            "       AND u.G_UFFICIO IN (SELECT * FROM JSON_TABLE(GET_UFF_GER(?5),'$[*]' COLUMNS VAL VARCHAR PATH '$')) " +
            "   ) OR " +
            "   ( " +
            "       ?6 != 'R_FP_LOCALE' " +
            "       AND ( " +
            "           u.G_UFFICIO IN (SELECT * FROM JSON_TABLE(GET_UFF_GER(?5),'$[*]' COLUMNS VAL VARCHAR PATH '$')) " +
            "           OR (u.FORZA_POLIZIA = GET_GROUP_ID(?5, 1) AND NOT EXISTS (SELECT * FROM USERS_ORARIO_LAVORO uol WHERE TRIM(uol.G_MEMBER) = TRIM(u.G_MEMBER) AND uol.DATA_CAN IS NULL)) " +
            "       ) " +
            "   ) " +
            ")",
            countQuery = "SELECT DISTINCT u.* " +
                    "FROM USERS u LEFT OUTER JOIN SEC_COMANDANTE_UFFICI scu ON TRIM(u.G_MEMBER) = TRIM(scu.UTE_CMD) " +
                    "WHERE TRIM(u.G_MEMBER) in(SELECT TRIM(USERNAME) FROM V_QUERYUTENZEABILITATE vq) " +
                    "AND u.DATA_CAN IS NULL " +
                    "AND (?1 IS NULL OR u.NOME LIKE ?1% OR u.COGNOME LIKE ?1% OR u.G_MEMBER LIKE ?1%) " +
                    "AND (?2 IS NULL OR u.FORZA_POLIZIA = ?2) " +
                    "AND (?3 IS NULL OR u.G_UFFICIO = ?3) " +
                    "AND (?4 = 0 OR (?4 = 1 AND scu.UTE_CMD IS NOT NULL) OR (?4 = 2 AND scu.UTE_CMD IS NULL)) " +
                    "AND ( " +
                    "    ( " +
                    "       ?6 = 'R_FP_LOCALE' " +
                    "       AND EXISTS (SELECT * FROM USERS_ORARIO_LAVORO uol WHERE TRIM(uol.G_MEMBER) = TRIM(u.G_MEMBER) AND uol.DATA_CAN IS NULL) " +
                    "       AND u.G_UFFICIO IN (SELECT * FROM JSON_TABLE(GET_UFF_GER(?5),'$[*]' COLUMNS VAL VARCHAR PATH '$')) " +
                    "   ) OR " +
                    "   ( " +
                    "       ?6 != 'R_FP_LOCALE' " +
                    "       AND ( " +
                    "           u.G_UFFICIO IN (SELECT * FROM JSON_TABLE(GET_UFF_GER(?5),'$[*]' COLUMNS VAL VARCHAR PATH '$')) " +
                    "           OR (u.FORZA_POLIZIA = GET_GROUP_ID(?5, 1) AND NOT EXISTS (SELECT * FROM USERS_ORARIO_LAVORO uol WHERE TRIM(uol.G_MEMBER) = TRIM(u.G_MEMBER) AND uol.DATA_CAN IS NULL)) " +
                    "       ) " +
                    "   ) " +
                    ")",
            nativeQuery = true)
    Page<Users> getUtentiAbilitatiByFiltro(String nomeCognome, Integer forzaPolizia, String codiceUfficio, int tipoComandante, String ufficioRichiedente,
                                           String ruoloUtenteRichiedente, Pageable pageable);

    @Query(value = "SELECT COUNT(*) " +
            "FROM USERS u " +
            "WHERE TRIM(u.G_MEMBER) in(SELECT TRIM(USERNAME) FROM V_QUERYUTENZEABILITATE vq)", nativeQuery = true)
    Integer getNumeroUtentiAbilitati();

    @Query(value = "SELECT STATO_ACCOUNT FROM V_QUERYUTENZEABILITATE " +
            "WHERE TRIM(USERNAME) = TRIM(?1)", nativeQuery = true)
    String getStatoAccount(String codiceUtente);

    @Query("FROM Users u WHERE TRIM(u.codiceUtente) = TRIM(?1) AND u.dataCancellazione IS NULL")
    Users getUtenteByCodiceUtente(String codiceUtente);

    @Modifying
    @Query(value = "UPDATE SEC_OIM_USER_STAGING SET STATO_ACCOUNT = 2 WHERE TRIM(USERNAME) = TRIM(?1)", nativeQuery = true)
    void unlockUser(String codiceUtente);

    @Query(value = "CALL UTILITY.SEND_MAIL(?1, ?2, ?3)",nativeQuery = true)
    void sendMail(String email, String subject, String messaggio);

    @Query(value = "SELECT u.CF " +
            "FROM USERS u " +
            "WHERE (u.EMAIL = ?1 OR u.EMAIL_PRIV = ?1) " +
            "AND u.CF <> ?2 " +
            "AND u.DATA_CAN IS NULL " +
            "UNION " +
            "SELECT s.CF " +
            "FROM SEC_RICHIESTA_REGISTRAZIONE s " +
            "WHERE (s.EMAIL = ?1 OR s.EMAIL_PRIV = ?1) " +
            "AND s.CF <> ?2 " +
            "AND s.STATO IN (1, 2)", nativeQuery = true)
    List<String> checkEmailAlreadyExists(String email, String codiceFiscale);

    @Query(value = "SELECT * FROM USERS WHERE (G_MEMBER LIKE %?1% OR COGNOME LIKE %?1%) AND FORZA_POLIZIA = ?2", nativeQuery = true)
    List<Users> getUsersByAutocompleteCodiceCognomeEFP(String parametro, Integer codiceForzaPolizia);

    @Query(value = "SELECT * FROM SEC_COMANDANTE_UFFICI s WHERE s.UFF_SEGN = ?1 AND s.DATA_CAN IS NULL", nativeQuery = true)
    Tuple checkComandantePresenteInUfficio(String codiceUfficio);

    @Query(value = "SELECT tu.ID_UTE FROM CCD.TSI035_UTENTE tu " +
            "WHERE TRIM(tu.ID_UTE) = TRIM(?1) " +
            "AND tu.D_REG_CAN IS NULL", nativeQuery = true)
    String utentePresenteSuSDI(String codiceUtente);

    @Query(value =
            "SELECT C.G_NAME " +
            "FROM SSD_SECURITY.SEC_APPLICAZIONE A, " +
            "     SSD_SECURITY.GROUPS B, " +
            "     SSD_SECURITY.GROUPMEMBERS C " +
            "WHERE " +
            "     A.APP_NAME = :nomeApplicazione " +
            " AND A.DATE_CANC IS NULL " +
            " AND B.G_APP = A.APP_ID " +
            " AND B.G_GROUP = :tipoGruppo " +
            " AND B.DATA_CAN IS NULL " +
            " AND C.G_NAME = B.G_NAME " +
            " AND C.G_MEMBER = :codiceUtente " +
            " AND C.DATA_CAN IS NULL", nativeQuery = true)
    List<String> getListaRuoliApplicazioneAbilitatoUtente(String codiceUtente, String nomeApplicazione, String tipoGruppo);

}
