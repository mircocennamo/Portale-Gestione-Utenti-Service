package it.interno.gestioneutentiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsersDto {
    private String codiceUtente;
    private String codiceRegione;
    private String codiceProvincia;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private Character sesso;
    private String luogoNascita;
    private String codiceFiscale;
    private String email;
    private String telefono;
    private Integer qualifica;
    private String descrizioneQualifica;
    private String ruolo;
    private String emailPrivata;
    private String utenteInserimento;
    private Timestamp dataInsertimento;
    private String ufficioInserimento;
    private String utenteAggiornamento;
    private Timestamp dataAggiornamento;
    private String utenteCancellazione;
    private String ufficioCancellazione;
    private Timestamp dataCancellazione;
    private Integer emailPrimaria;
    private String siglaProvinciaNacita;
    private String idFunzione;
    private Integer idTipoUtente;
    private String paeseDiNascita;
    private String note;
    private String prefissoTelefono;
    private LuogoDto codiceLuogoNascita;
    private ForzaPoliziaDto forzaPolizia;
    private UfficioDto codiceUfficio;

    private String statoAccount; // Si prende dalla vista SEC_OIM_USER_STAGING
    private OrarioLavoroDto orarioLavoro;
    private List<UfficioDto> ufficiComandati;
    private String categoria;
}
