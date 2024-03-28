package it.interno.gestioneutentiservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SEC_QUALIFICA", schema = "SSD_SECURITY")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Qualifica {

    @Id
    @Column(name = "QUALIFICA_ID")
    private Integer idQualifica;
    @Column(name = "CATEGORIA")
    private String categoria;
    @Column(name = "RUOLO")
    private String ruolo;
    @Column(name = "QUALIFICA_GRADO")
    private String grado;
    @Column(name = "POLIZIA_DI_STATO")
    private String polizia;
    @Column(name = "ARMA_DEI_CARABINIERI")
    private String carabinieri;
    @Column(name = "CORPO_DELLA_GUARDIA_DI_FINANZA")
    private String guardiaDiFinanza;
    @Column(name = "CORPO_DI_POLIZIA_PENITENZIARIA")
    private String poliziaPenitenziaria;
    @Column(name = "PERSONALE_CIVILE")
    private String personaleCivile;
}
