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
    UsersDto toDto(Users entity, @Context UsersRepository repoUsers);
}
