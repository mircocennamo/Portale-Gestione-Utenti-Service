package it.interno.gestioneutentiservice.mapper;

import it.interno.gestioneutentiservice.dto.OrarioLavoroDto;
import it.interno.gestioneutentiservice.entity.OrarioLavoro;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface OrarioLavoroMapper {

    OrarioLavoro toEntity(OrarioLavoroDto dto);

    @InheritInverseConfiguration
    OrarioLavoroDto toDto(OrarioLavoro entity);
}
