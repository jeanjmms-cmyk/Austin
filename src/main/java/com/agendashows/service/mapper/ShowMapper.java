package com.agendashows.service.mapper;

import com.agendashows.domain.Cantor;
import com.agendashows.domain.Show;
import com.agendashows.service.dto.CantorDTO;
import com.agendashows.service.dto.ShowDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Show} and its DTO {@link ShowDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShowMapper extends EntityMapper<ShowDTO, Show> {
    @Mapping(target = "cantor", source = "cantor", qualifiedByName = "cantorNome")
    ShowDTO toDto(Show s);

    @Named("cantorNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    CantorDTO toDtoCantorNome(Cantor cantor);
}
