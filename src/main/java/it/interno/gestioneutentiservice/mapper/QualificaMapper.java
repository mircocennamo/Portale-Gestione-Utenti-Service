package it.interno.gestioneutentiservice.mapper;

import it.interno.gestioneutentiservice.dto.QualificaDto;
import it.interno.gestioneutentiservice.entity.Qualifica;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface QualificaMapper {

    Qualifica toEntity(QualificaDto qualificaDto);

    @InheritInverseConfiguration
    QualificaDto toDto(Qualifica qualifica);
}
