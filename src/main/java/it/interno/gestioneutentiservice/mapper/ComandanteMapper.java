package it.interno.gestioneutentiservice.mapper;

import it.interno.gestioneutentiservice.dto.ComandanteDto;
import it.interno.gestioneutentiservice.entity.Comandante;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ComandanteMapper {

    Comandante toEntity(ComandanteDto dto);

    @InheritInverseConfiguration
    ComandanteDto toDto(Comandante entity);
}
