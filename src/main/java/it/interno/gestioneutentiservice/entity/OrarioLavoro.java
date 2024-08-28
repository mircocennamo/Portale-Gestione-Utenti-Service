package it.interno.gestioneutentiservice.entity;

import it.interno.gestioneutentiservice.entity.pk.OrarioLavoroPK;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "USERS_ORARIO_LAVORO", schema = "SSD_SECURITY")
@NoArgsConstructor
@AllArgsConstructor
@Data
@IdClass(OrarioLavoroPK.class)
public class OrarioLavoro {

    @Id
    @Column(name = "G_MEMBER")
    private String codiceUtente;
    @Id
    @Column(name = "G_UFFICIO")
    private String codiceUfficio;
    @Id
    @Column(name = "DATE_INS")
    private Timestamp dataInsermento;
    @Column(name = "UTE_INS")
    private String utenteInserimento;
    @Column(name = "UFF_INS")
    private String ufficioInserimento;
    @Column(name = "DATA_CAN")
    private Timestamp dataCancellazione;
    @Column(name = "UTE_CAN")
    private String utenteCancellazione;
    @Column(name = "UFF_CAN")
    private String ufficioCancellazione;
    @Column(name = "DATE_SCADENZA_UTENTE")
    private LocalDate scadenzaUtente;
    @Column(name = "O_LUN_DA")
    private String lunediDa;
    @Column(name = "O_LUN_A")
    private String lunediA;
    @Column(name = "O_MAR_DA")
    private String martediDa;
    @Column(name = "O_MAR_A")
    private String martediA;
    @Column(name = "O_MER_DA")
    private String mercolediDa;
    @Column(name = "O_MER_A")
    private String mercolediA;
    @Column(name = "O_GIO_DA")
    private String giovediDa;
    @Column(name = "O_GIO_A")
    private String giovediA;
    @Column(name = "O_VEN_DA")
    private String venerdiDa;
    @Column(name = "O_VEN_A")
    private String venerdiA;
    @Column(name = "O_SAB_DA")
    private String sabatoDa;
    @Column(name = "O_SAB_A")
    private String sabatoA;
    @Column(name = "O_DOM_DA")
    private String domenicaDa;
    @Column(name = "O_DOM_A")
    private String domenicaA;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrarioLavoro that = (OrarioLavoro) o;
        return getCodiceUtente().equals(that.getCodiceUtente()) && getCodiceUfficio().equals(that.getCodiceUfficio()) && Objects.equals(getScadenzaUtente(), that.getScadenzaUtente()) && getLunediDa().equals(that.getLunediDa()) && getLunediA().equals(that.getLunediA()) && getMartediDa().equals(that.getMartediDa()) && getMartediA().equals(that.getMartediA()) && getMercolediDa().equals(that.getMercolediDa()) && getMercolediA().equals(that.getMercolediA()) && getGiovediDa().equals(that.getGiovediDa()) && getGiovediA().equals(that.getGiovediA()) && getVenerdiDa().equals(that.getVenerdiDa()) && getVenerdiA().equals(that.getVenerdiA()) && getSabatoDa().equals(that.getSabatoDa()) && getSabatoA().equals(that.getSabatoA()) && getDomenicaDa().equals(that.getDomenicaDa()) && getDomenicaA().equals(that.getDomenicaA());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodiceUtente(), getCodiceUfficio(), getScadenzaUtente(), getLunediDa(), getLunediA(), getMartediDa(), getMartediA(), getMercolediDa(), getMercolediA(), getGiovediDa(), getGiovediA(), getVenerdiDa(), getVenerdiA(), getSabatoDa(), getSabatoA(), getDomenicaDa(), getDomenicaA());
    }
}

