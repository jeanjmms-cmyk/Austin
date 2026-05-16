package com.agendashows.service.mapper;

import com.agendashows.domain.Cantor;
import com.agendashows.service.dto.CantorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cantor} and its DTO {@link CantorDTO}.
 */
@Mapper(componentModel = "spring")
public interface CantorMapper extends EntityMapper<CantorDTO, Cantor> {}
