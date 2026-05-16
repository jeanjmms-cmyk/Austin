package com.agendashows.service;

import com.agendashows.domain.*; // for static metamodels
import com.agendashows.domain.Show;
import com.agendashows.repository.ShowRepository;
import com.agendashows.service.criteria.ShowCriteria;
import com.agendashows.service.dto.ShowDTO;
import com.agendashows.service.mapper.ShowMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Show} entities in the database.
 * The main input is a {@link ShowCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ShowDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShowQueryService extends QueryService<Show> {

    private static final Logger LOG = LoggerFactory.getLogger(ShowQueryService.class);

    private final ShowRepository showRepository;

    private final ShowMapper showMapper;

    public ShowQueryService(ShowRepository showRepository, ShowMapper showMapper) {
        this.showRepository = showRepository;
        this.showMapper = showMapper;
    }

    /**
     * Return a {@link Page} of {@link ShowDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShowDTO> findByCriteria(ShowCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Show> specification = createSpecification(criteria);
        return showRepository.findAll(specification, page).map(showMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShowCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Show> specification = createSpecification(criteria);
        return showRepository.count(specification);
    }

    /**
     * Function to convert {@link ShowCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Show> createSpecification(ShowCriteria criteria) {
        Specification<Show> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Show_.id),
                buildStringSpecification(criteria.getLocal(), Show_.local),
                buildRangeSpecification(criteria.getDataShow(), Show_.dataShow),
                buildRangeSpecification(criteria.getHorarioInicio(), Show_.horarioInicio),
                buildSpecification(criteria.getStatus(), Show_.status),
                buildSpecification(criteria.getCantorId(), root -> root.join(Show_.cantor, JoinType.LEFT).get(Cantor_.id))
            );
        }
        return specification;
    }
}
