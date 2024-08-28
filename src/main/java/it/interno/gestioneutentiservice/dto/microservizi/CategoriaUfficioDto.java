package it.interno.gestioneutentiservice.dto.microservizi;

import it.interno.gestioneutentiservice.dto.ForzaPoliziaDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoriaUfficioDto {
    private String codiceCategoriaUfficio;
    private ForzaPoliziaDto forzaPolizia;
}
