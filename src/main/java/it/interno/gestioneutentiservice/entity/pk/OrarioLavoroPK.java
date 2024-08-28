package it.interno.gestioneutentiservice.entity.pk;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrarioLavoroPK implements Serializable {
    private String codiceUtente;
    private String codiceUfficio;
    private Timestamp dataInsermento;
}
