package it.interno.gestioneutentiservice.entity.microservizi;

import it.interno.gestioneutentiservice.entity.ForzaPolizia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoriaUfficioPK implements Serializable {
    private String codiceCategoriaUfficio;
    private ForzaPolizia forzaPolizia;
}
