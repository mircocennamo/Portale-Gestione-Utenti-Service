package it.interno.gestioneutentiservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "TLUOGO", schema = "SSD_SECURITY")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Luogo {

    @Id
    @Column(name = "CODICELUOGO")
    private Integer codiceLuogo;
    @Column(name = "DESCRIZIONELUOGO")
    private String descrizioneLuogo;
    @Column(name = "INLUOGO")
    private String inLuogo;
    @Column(name = "DATAINIZIOVALIDITA")
    private LocalDate dataInizioValidita;
    @Column(name = "DATAFINEVALIDITA")
    private LocalDate dataFineValidita;
    @Column(name = "TSCANCELLAZIONE")
    private Timestamp tsCancellazione;
    @Column(name = "CODICEREGIONE")
    private String codiceRegione;
    @Column(name = "CODICEPROVINCIA")
    private String codiceProvincia;
    @Column(name = "SIGLAPROVINCIA")
    private String siglaProvincia;

}
