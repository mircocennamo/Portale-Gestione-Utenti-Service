package it.interno.gestioneutentiservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "USERS", schema = "SSD_SECURITY")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Users {

    @Id
    @Column(name = "G_MEMBER")
    private String codiceUtente;
    @Column(name = "COD_REG")
    private String codiceRegione;
    @Column(name = "COD_PRV")
    private String codiceProvincia;
    @Column(name = "NOME")
    private String nome;
    @Column(name = "COGNOME")
    private String cognome;
    @Column(name = "DATADINASCITA")
    private LocalDate dataNascita;
    @Column(name = "SESSO")
    private Character sesso;
    @Column(name = "LUOGODINASCITA")
    private String luogoNascita;
    @Column(name = "CF")
    private String codiceFiscale;
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "TELEFONO")
    private String telefono;
    @Column(name = "QUALIFICA")
    private Integer qualifica;
    @Column(name = "EMAIL_PRIV")
    private String emailPrivata;
    @Column(name = "UTE_INS")
    private String utenteInserimento;
    @Column(name = "DATE_INS")
    private Timestamp dataInsertimento;
    @Column(name = "UFF_INS")
    private String ufficioInserimento;
    @Column(name = "UTE_AGG")
    private String utenteAggiornamento;
    @Column(name = "DATA_AGG")
    private Timestamp dataAggiornamento;
    @Column(name = "UTE_CAN")
    private String utenteCancellazione;
    @Column(name = "UFF_CAN")
    private String ufficioCancellazione;
    @Column(name = "DATA_CAN")
    private Timestamp dataCancellazione;
    @Column(name = "PRIMARY_EMAIL")
    private Integer emailPrimaria;
    @Column(name = "SIGLAPROVINCIADINASCITA")
    private String siglaProvinciaNacita;
    @Column(name = "ID_FUNZIONE")
    private String idFunzione;
    @Column(name = "IDTIPOUTENTE")
    private Integer idTipoUtente;
    @Column(name = "PAESEDINASCITA")
    private String paeseDiNascita;
    @Column(name = "NOTEUTENTE")
    private String note;
    @Column(name = "TELEFONO_PRE")
    private String prefissoTelefono;

    @ManyToOne
    @JoinColumn(name = "CODICELUOGODINASCITA", referencedColumnName = "CODICELUOGO")
    private Luogo codiceLuogoNascita;

    @ManyToOne
    @JoinColumn(name = "FORZA_POLIZIA", referencedColumnName = "GROUP_ID")
    private ForzaPolizia forzaPolizia;

    @ManyToOne
    @JoinColumn(name = "G_UFFICIO", referencedColumnName = "COD_UFF")
    private Ufficio codiceUfficio;

}
