package it.interno.gestioneutentiservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "SEC_FUNZIONE", schema = "SSD_SECURITY")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Funzione {

    @Id
    @Column(name = "ID_FUNZIONE")
    private String idFunzione;
    @Column(name = "FUNZIONE")
    private String descrizioneFunzione;
    @Column(name = "DATE_CAN")
    private Timestamp dataCancellazione;

}
