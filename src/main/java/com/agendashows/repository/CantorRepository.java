package com.agendashows.repository;

import com.agendashows.domain.Cantor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cantor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CantorRepository extends JpaRepository<Cantor, Long>, JpaSpecificationExecutor<Cantor> {}
