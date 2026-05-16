package com.agendashows.web.rest;

import com.agendashows.repository.ShowRepository;
import com.agendashows.service.ShowQueryService;
import com.agendashows.service.ShowService;
import com.agendashows.service.criteria.ShowCriteria;
import com.agendashows.service.dto.ShowDTO;
import com.agendashows.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.agendashows.domain.Show}.
 */
@RestController
@RequestMapping("/api/shows")
public class ShowResource {

    private static final Logger LOG = LoggerFactory.getLogger(ShowResource.class);

    private static final String ENTITY_NAME = "show";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShowService showService;

    private final ShowRepository showRepository;

    private final ShowQueryService showQueryService;

    public ShowResource(ShowService showService, ShowRepository showRepository, ShowQueryService showQueryService) {
        this.showService = showService;
        this.showRepository = showRepository;
        this.showQueryService = showQueryService;
    }

    /**
     * {@code POST  /shows} : Create a new show.
     *
     * @param showDTO the showDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new showDTO, or with status {@code 400 (Bad Request)} if the show has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ShowDTO> createShow(@Valid @RequestBody ShowDTO showDTO) throws URISyntaxException {
        LOG.debug("REST request to save Show : {}", showDTO);
        if (showDTO.getId() != null) {
            throw new BadRequestAlertException("A new show cannot already have an ID", ENTITY_NAME, "idexists");
        }
        showDTO = showService.save(showDTO);
        return ResponseEntity.created(new URI("/api/shows/" + showDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, showDTO.getId().toString()))
            .body(showDTO);
    }

    /**
     * {@code PUT  /shows/:id} : Updates an existing show.
     *
     * @param id the id of the showDTO to save.
     * @param showDTO the showDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated showDTO,
     * or with status {@code 400 (Bad Request)} if the showDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the showDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ShowDTO> updateShow(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShowDTO showDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Show : {}, {}", id, showDTO);
        if (showDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, showDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!showRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        showDTO = showService.update(showDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, showDTO.getId().toString()))
            .body(showDTO);
    }

    /**
     * {@code PATCH  /shows/:id} : Partial updates given fields of an existing show, field will ignore if it is null
     *
     * @param id the id of the showDTO to save.
     * @param showDTO the showDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated showDTO,
     * or with status {@code 400 (Bad Request)} if the showDTO is not valid,
     * or with status {@code 404 (Not Found)} if the showDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the showDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShowDTO> partialUpdateShow(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShowDTO showDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Show partially : {}, {}", id, showDTO);
        if (showDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, showDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!showRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShowDTO> result = showService.partialUpdate(showDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, showDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shows} : get all the shows.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shows in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ShowDTO>> getAllShows(
        ShowCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Shows by criteria: {}", criteria);

        Page<ShowDTO> page = showQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shows/count} : count all the shows.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countShows(ShowCriteria criteria) {
        LOG.debug("REST request to count Shows by criteria: {}", criteria);
        return ResponseEntity.ok().body(showQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shows/:id} : get the "id" show.
     *
     * @param id the id of the showDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the showDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ShowDTO> getShow(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Show : {}", id);
        Optional<ShowDTO> showDTO = showService.findOne(id);
        return ResponseUtil.wrapOrNotFound(showDTO);
    }

    /**
     * {@code DELETE  /shows/:id} : delete the "id" show.
     *
     * @param id the id of the showDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShow(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Show : {}", id);
        showService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
