package it.interno.gestioneutentiservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.interno.gestioneutentiservice.serializer.TimestampDeserializer;
import it.interno.gestioneutentiservice.serializer.TimestampSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ComandanteDto {
    private UfficioDto codiceUfficio;
    private UsersDto utente;
    private String utenteInserimento;
    private String ufficioInserimento;
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp dataInserimento;
    private String utenteCancellazione;
    private String ufficioCancellazione;
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp dataCancellazione;
}
