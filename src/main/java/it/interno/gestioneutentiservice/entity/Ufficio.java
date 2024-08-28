package it.interno.gestioneutentiservice.entity;

import it.interno.gestioneutentiservice.entity.microservizi.CategoriaUfficio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "SEC_UFFICIO_LEVEL", schema = "SSD_SECURITY")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Ufficio {

    @Id
    @Column(name = "COD_UFF")
    private String codiceUfficio;
    @Column(name = "UFFICIO")
    private String descrizioneUfficio;
    @Column(name = "DATA_INIZIO")
    private LocalDate dataInizio;
    @Column(name = "DATA_FINE")
    private LocalDate dataFine;
    @Column(name = "DATA_CAN")
    private Timestamp dataCancellazione;

    @ManyToOne
    @JoinColumn(name = "FORZA_POLIZIA", referencedColumnName = "GROUP_ID")
    @JoinColumn(name = "COD_CATEG_UFF", referencedColumnName = "COD_CATEG_UFF")
    private CategoriaUfficio categoriaUfficio;

    @ManyToOne
    @JoinColumn(name = "CODICELUOGOUFFICIO", referencedColumnName = "CODICELUOGO")
    private Luogo luogoUfficio;
}

