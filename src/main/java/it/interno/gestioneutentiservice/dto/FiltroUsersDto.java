package it.interno.gestioneutentiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FiltroUsersDto {

    private String nomeCognome;
    private Integer forzaPolizia;
    private String codiceUfficio;
    private int tipoComandante;
    private PaginazioneDto paginazione;
}
