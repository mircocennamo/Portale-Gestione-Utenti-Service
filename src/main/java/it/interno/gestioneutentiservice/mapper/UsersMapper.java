package it.interno.gestioneutentiservice.mapper;

import it.interno.gestioneutentiservice.dto.UsersDto;
import it.interno.gestioneutentiservice.entity.Users;
import it.interno.gestioneutentiservice.repository.UsersRepository;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface UsersMapper {

    Users toEntity(UsersDto dto);

    @InheritInverseConfiguration
    @Mapping(target = "statoAccount", expression = "java(repoUsers.getStatoAccount(entity.getCodiceUtente()))")
    @Mapping(target = "categoria", expression = "java(repoUsers.getCategoriaByIdQualifica(entity.getQualifica()))")
    @Mapping(target = "descrizioneQualifica", expression = "java(repoUsers.getDescrizioneQualificaByIdEEnte(entity.getQualifica(), entity.getForzaPolizia().getIdGruppo()))")
    @Mapping(target = "ruolo", expression = "java(repoUsers.getRuoloByQualifica(entity.getQualifica()))")
    UsersDto toDto(Users entity, @Context UsersRepository repoUsers);
}
