package it.interno.gestioneutentiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class QualificaDto {
    private Integer idQualifica;
    private String categoria;
    private String ruolo;
    private String grado;
    private String polizia;
    private String carabinieri;
    private String guardiaDiFinanza;
    private String poliziaPenitenziaria;
    private String personaleCivile;
}
