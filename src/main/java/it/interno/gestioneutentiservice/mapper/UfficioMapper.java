package it.interno.gestioneutentiservice.mapper;

import it.interno.gestioneutentiservice.dto.UfficioDto;
import it.interno.gestioneutentiservice.entity.Ufficio;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UfficioMapper {

    Ufficio toEntity(UfficioDto dto);

    @InheritInverseConfiguration
    UfficioDto toDto(Ufficio entity);
}
