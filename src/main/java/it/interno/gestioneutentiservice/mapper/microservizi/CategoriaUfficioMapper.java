package it.interno.gestioneutentiservice.mapper.microservizi;

import it.interno.gestioneutentiservice.dto.microservizi.CategoriaUfficioDto;
import it.interno.gestioneutentiservice.entity.microservizi.CategoriaUfficio;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CategoriaUfficioMapper {

    CategoriaUfficio toEntity(CategoriaUfficioDto dto);

    @InheritInverseConfiguration
    CategoriaUfficioDto toDto(CategoriaUfficio entity);
}
