package it.interno.gestioneutentiservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.interno.gestioneutentiservice.serializer.LocalDateDeserializer;
import it.interno.gestioneutentiservice.serializer.LocalDateSerializer;
import it.interno.gestioneutentiservice.serializer.TimestampDeserializer;
import it.interno.gestioneutentiservice.serializer.TimestampSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LuogoDto {
    private Integer codiceLuogo;
    private String descrizioneLuogo;
    private String inLuogo;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dataInizioValidita;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dataFineValidita;
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp tsCancellazione;
    private String codiceRegione;
    private String codiceProvincia;
    private String siglaProvincia;
}
