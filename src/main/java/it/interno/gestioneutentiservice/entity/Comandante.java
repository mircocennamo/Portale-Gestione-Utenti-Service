package it.interno.gestioneutentiservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "SEC_COMANDANTE_UFFICI", schema = "SSD_SECURITY")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Comandante implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "UFF_SEGN", referencedColumnName = "COD_UFF")
    private Ufficio codiceUfficio;

    @Id
    @ManyToOne
    @JoinColumn(name = "UTE_CMD", referencedColumnName = "G_MEMBER")
    private Users utente;

    @Column(name = "UTE_INS")
    private String utenteInserimento;
    @Column(name = "UFF_INS")
    private String ufficioInserimento;
    @Column(name = "DATA_INS")
    private Timestamp dataInserimento;
    @Column(name = "UTE_CAN")
    private String utenteCancellazione;
    @Column(name = "UFF_CAN")
    private String ufficioCancellazione;
    @Column(name = "DATA_CAN")
    private Timestamp dataCancellazione;
}
