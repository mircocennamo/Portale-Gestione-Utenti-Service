package it.interno.gestioneutentiservice.mapper;

import it.interno.gestioneutentiservice.dto.FunzioneDto;
import it.interno.gestioneutentiservice.entity.Funzione;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface FunzioneMapper {

    Funzione toEntity(FunzioneDto funzioneDto);

    @InheritInverseConfiguration
    FunzioneDto toDto(Funzione funzione);
}
