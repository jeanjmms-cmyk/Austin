package com.agendashows.service.impl;

import com.agendashows.domain.Cantor;
import com.agendashows.repository.CantorRepository;
import com.agendashows.service.CantorService;
import com.agendashows.service.dto.CantorDTO;
import com.agendashows.service.mapper.CantorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.agendashows.domain.Cantor}.
 */
@Service
@Transactional
public class CantorServiceImpl implements CantorService {

    private static final Logger LOG = LoggerFactory.getLogger(CantorServiceImpl.class);

    private final CantorRepository cantorRepository;

    private final CantorMapper cantorMapper;

    public CantorServiceImpl(CantorRepository cantorRepository, CantorMapper cantorMapper) {
        this.cantorRepository = cantorRepository;
        this.cantorMapper = cantorMapper;
    }

    @Override
    public CantorDTO save(CantorDTO cantorDTO) {
        LOG.debug("Request to save Cantor : {}", cantorDTO);
        Cantor cantor = cantorMapper.toEntity(cantorDTO);
        cantor = cantorRepository.save(cantor);
        return cantorMapper.toDto(cantor);
    }

    @Override
    public CantorDTO update(CantorDTO cantorDTO) {
        LOG.debug("Request to update Cantor : {}", cantorDTO);
        Cantor cantor = cantorMapper.toEntity(cantorDTO);
        cantor = cantorRepository.save(cantor);
        return cantorMapper.toDto(cantor);
    }

    @Override
    public Optional<CantorDTO> partialUpdate(CantorDTO cantorDTO) {
        LOG.debug("Request to partially update Cantor : {}", cantorDTO);

        return cantorRepository
            .findById(cantorDTO.getId())
            .map(existingCantor -> {
                cantorMapper.partialUpdate(existingCantor, cantorDTO);

                return existingCantor;
            })
            .map(cantorRepository::save)
            .map(cantorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CantorDTO> findOne(Long id) {
        LOG.debug("Request to get Cantor : {}", id);
        return cantorRepository.findById(id).map(cantorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Cantor : {}", id);
        cantorRepository.deleteById(id);
    }
}
