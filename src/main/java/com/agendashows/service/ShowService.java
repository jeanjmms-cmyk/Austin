package com.agendashows.service;

import com.agendashows.service.dto.ShowDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.agendashows.domain.Show}.
 */
public interface ShowService {
    /**
     * Save a show.
     *
     * @param showDTO the entity to save.
     * @return the persisted entity.
     */
    ShowDTO save(ShowDTO showDTO);

    /**
     * Updates a show.
     *
     * @param showDTO the entity to update.
     * @return the persisted entity.
     */
    ShowDTO update(ShowDTO showDTO);

    /**
     * Partially updates a show.
     *
     * @param showDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ShowDTO> partialUpdate(ShowDTO showDTO);

    /**
     * Get all the shows with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ShowDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" show.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ShowDTO> findOne(Long id);

    /**
     * Delete the "id" show.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
