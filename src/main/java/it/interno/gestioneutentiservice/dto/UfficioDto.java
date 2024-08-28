package it.interno.gestioneutentiservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.interno.gestioneutentiservice.dto.microservizi.CategoriaUfficioDto;
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
public class UfficioDto {
    private String codiceUfficio;
    private String descrizioneUfficio;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dataInizio;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate dataFine;
    @JsonSerialize(using = TimestampSerializer.class)
    @JsonDeserialize(using = TimestampDeserializer.class)
    private Timestamp dataCancellazione;
    private CategoriaUfficioDto categoriaUfficio;
    private LuogoDto luogoUfficio;
}
