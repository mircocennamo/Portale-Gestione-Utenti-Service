package it.interno.gestioneutentiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaginazioneDto {
    private Integer numeroPagina;
    private Integer numeroElementi;
    private String orderBy;
    private String orderDirection;
}
