package com.agendashows.service;

import com.agendashows.domain.*; // for static metamodels
import com.agendashows.domain.Cantor;
import com.agendashows.repository.CantorRepository;
import com.agendashows.service.criteria.CantorCriteria;
import com.agendashows.service.dto.CantorDTO;
import com.agendashows.service.mapper.CantorMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Cantor} entities in the database.
 * The main input is a {@link CantorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CantorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CantorQueryService extends QueryService<Cantor> {

    private static final Logger LOG = LoggerFactory.getLogger(CantorQueryService.class);

    private final CantorRepository cantorRepository;

    private final CantorMapper cantorMapper;

    public CantorQueryService(CantorRepository cantorRepository, CantorMapper cantorMapper) {
        this.cantorRepository = cantorRepository;
        this.cantorMapper = cantorMapper;
    }

    /**
     * Return a {@link Page} of {@link CantorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CantorDTO> findByCriteria(CantorCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cantor> specification = createSpecification(criteria);
        return cantorRepository.findAll(specification, page).map(cantorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CantorCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Cantor> specification = createSpecification(criteria);
        return cantorRepository.count(specification);
    }

    /**
     * Function to convert {@link CantorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cantor> createSpecification(CantorCriteria criteria) {
        Specification<Cantor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Cantor_.id),
                buildStringSpecification(criteria.getNome(), Cantor_.nome),
                buildStringSpecification(criteria.getGeneroMusical(), Cantor_.generoMusical),
                buildStringSpecification(criteria.getFotoPerfil(), Cantor_.fotoPerfil),
                buildSpecification(criteria.getAtivo(), Cantor_.ativo)
            );
        }
        return specification;
    }
}
