package it.interno.gestioneutentiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrarioLavoroDto {

    private String codiceUtente;
    private String codiceUfficio;
    private Timestamp dataInsermento;
    private String utenteInserimento;
    private String ufficioInserimento;
    private Timestamp dataCancellazione;
    private String utenteCancellazione;
    private String ufficioCancellazione;
    private LocalDate scadenzaUtente;
    private String lunediDa;
    private String lunediA;
    private String martediDa;
    private String martediA;
    private String mercolediDa;
    private String mercolediA;
    private String giovediDa;
    private String giovediA;
    private String venerdiDa;
    private String venerdiA;
    private String sabatoDa;
    private String sabatoA;
    private String domenicaDa;
    private String domenicaA;
}
