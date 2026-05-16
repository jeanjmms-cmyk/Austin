package com.agendashows.service.impl;

import com.agendashows.domain.Show;
import com.agendashows.repository.ShowRepository;
import com.agendashows.service.ShowService;
import com.agendashows.service.dto.ShowDTO;
import com.agendashows.service.mapper.ShowMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agendashows.domain.Show}.
 */
@Service
@Transactional
public class ShowServiceImpl implements ShowService {

    private static final Logger LOG = LoggerFactory.getLogger(ShowServiceImpl.class);

    private final ShowRepository showRepository;

    private final ShowMapper showMapper;

    public ShowServiceImpl(ShowRepository showRepository, ShowMapper showMapper) {
        this.showRepository = showRepository;
        this.showMapper = showMapper;
    }

    @Override
    public ShowDTO save(ShowDTO showDTO) {
        LOG.debug("Request to save Show : {}", showDTO);
        Show show = showMapper.toEntity(showDTO);
        show = showRepository.save(show);
        return showMapper.toDto(show);
    }

    @Override
    public ShowDTO update(ShowDTO showDTO) {
        LOG.debug("Request to update Show : {}", showDTO);
        Show show = showMapper.toEntity(showDTO);
        show = showRepository.save(show);
        return showMapper.toDto(show);
    }

    @Override
    public Optional<ShowDTO> partialUpdate(ShowDTO showDTO) {
        LOG.debug("Request to partially update Show : {}", showDTO);

        return showRepository
            .findById(showDTO.getId())
            .map(existingShow -> {
                showMapper.partialUpdate(existingShow, showDTO);

                return existingShow;
            })
            .map(showRepository::save)
            .map(showMapper::toDto);
    }

    public Page<ShowDTO> findAllWithEagerRelationships(Pageable pageable) {
        return showRepository.findAllWithEagerRelationships(pageable).map(showMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShowDTO> findOne(Long id) {
        LOG.debug("Request to get Show : {}", id);
        return showRepository.findOneWithEagerRelationships(id).map(showMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Show : {}", id);
        showRepository.deleteById(id);
    }
}
