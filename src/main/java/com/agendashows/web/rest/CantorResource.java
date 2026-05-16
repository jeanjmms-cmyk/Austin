package com.agendashows.web.rest;

import com.agendashows.repository.CantorRepository;
import com.agendashows.service.CantorQueryService;
import com.agendashows.service.CantorService;
import com.agendashows.service.criteria.CantorCriteria;
import com.agendashows.service.dto.CantorDTO;
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
 * REST controller for managing {@link com.agendashows.domain.Cantor}.
 */
@RestController
@RequestMapping("/api/cantors")
public class CantorResource {

    private static final Logger LOG = LoggerFactory.getLogger(CantorResource.class);

    private static final String ENTITY_NAME = "cantor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CantorService cantorService;

    private final CantorRepository cantorRepository;

    private final CantorQueryService cantorQueryService;

    public CantorResource(CantorService cantorService, CantorRepository cantorRepository, CantorQueryService cantorQueryService) {
        this.cantorService = cantorService;
        this.cantorRepository = cantorRepository;
        this.cantorQueryService = cantorQueryService;
    }

    /**
     * {@code POST  /cantors} : Create a new cantor.
     *
     * @param cantorDTO the cantorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cantorDTO, or with status {@code 400 (Bad Request)} if the cantor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CantorDTO> createCantor(@Valid @RequestBody CantorDTO cantorDTO) throws URISyntaxException {
        LOG.debug("REST request to save Cantor : {}", cantorDTO);
        if (cantorDTO.getId() != null) {
            throw new BadRequestAlertException("A new cantor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cantorDTO = cantorService.save(cantorDTO);
        return ResponseEntity.created(new URI("/api/cantors/" + cantorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cantorDTO.getId().toString()))
            .body(cantorDTO);
    }

    /**
     * {@code PUT  /cantors/:id} : Updates an existing cantor.
     *
     * @param id the id of the cantorDTO to save.
     * @param cantorDTO the cantorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cantorDTO,
     * or with status {@code 400 (Bad Request)} if the cantorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cantorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CantorDTO> updateCantor(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CantorDTO cantorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Cantor : {}, {}", id, cantorDTO);
        if (cantorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cantorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cantorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cantorDTO = cantorService.update(cantorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cantorDTO.getId().toString()))
            .body(cantorDTO);
    }

    /**
     * {@code PATCH  /cantors/:id} : Partial updates given fields of an existing cantor, field will ignore if it is null
     *
     * @param id the id of the cantorDTO to save.
     * @param cantorDTO the cantorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cantorDTO,
     * or with status {@code 400 (Bad Request)} if the cantorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cantorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cantorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CantorDTO> partialUpdateCantor(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CantorDTO cantorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Cantor partially : {}, {}", id, cantorDTO);
        if (cantorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cantorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cantorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CantorDTO> result = cantorService.partialUpdate(cantorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cantorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cantors} : get all the cantors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cantors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CantorDTO>> getAllCantors(
        CantorCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Cantors by criteria: {}", criteria);

        Page<CantorDTO> page = cantorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cantors/count} : count all the cantors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCantors(CantorCriteria criteria) {
        LOG.debug("REST request to count Cantors by criteria: {}", criteria);
        return ResponseEntity.ok().body(cantorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cantors/:id} : get the "id" cantor.
     *
     * @param id the id of the cantorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cantorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CantorDTO> getCantor(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Cantor : {}", id);
        Optional<CantorDTO> cantorDTO = cantorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cantorDTO);
    }

    /**
     * {@code DELETE  /cantors/:id} : delete the "id" cantor.
     *
     * @param id the id of the cantorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCantor(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Cantor : {}", id);
        cantorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
