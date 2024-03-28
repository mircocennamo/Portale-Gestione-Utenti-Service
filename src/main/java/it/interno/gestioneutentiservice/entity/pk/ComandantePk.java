package it.interno.gestioneutentiservice.entity.pk;

import it.interno.gestioneutentiservice.entity.Ufficio;
import it.interno.gestioneutentiservice.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ComandantePk implements Serializable {
    private Ufficio codiceUfficio;
    private Users utente;
}
