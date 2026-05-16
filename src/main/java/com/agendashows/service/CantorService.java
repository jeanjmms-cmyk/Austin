package com.agendashows.service;

import com.agendashows.service.dto.CantorDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.agendashows.domain.Cantor}.
 */
public interface CantorService {
    /**
     * Save a cantor.
     *
     * @param cantorDTO the entity to save.
     * @return the persisted entity.
     */
    CantorDTO save(CantorDTO cantorDTO);

    /**
     * Updates a cantor.
     *
     * @param cantorDTO the entity to update.
     * @return the persisted entity.
     */
    CantorDTO update(CantorDTO cantorDTO);

    /**
     * Partially updates a cantor.
     *
     * @param cantorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CantorDTO> partialUpdate(CantorDTO cantorDTO);

    /**
     * Get the "id" cantor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CantorDTO> findOne(Long id);

    /**
     * Delete the "id" cantor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
