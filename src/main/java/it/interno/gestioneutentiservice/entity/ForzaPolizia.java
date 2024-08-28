package it.interno.gestioneutentiservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SEC_FORZA_POLIZIA", schema = "SSD_SECURITY")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ForzaPolizia {

    @Id
    @Column(name = "GROUP_ID")
    private Integer idGruppo;
    @Column(name = "ORDER_ID")
    private Integer idOrdinamento;
    @Column(name = "GROUP_SHORTNAME")
    private String nomeAbbreviato;
    @Column(name = "GROUP_NAME")
    private String nome;

}
