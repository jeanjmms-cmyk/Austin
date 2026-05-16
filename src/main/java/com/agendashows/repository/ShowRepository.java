package com.agendashows.repository;

import com.agendashows.domain.Show;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Show entity.
 */
@Repository
public interface ShowRepository extends JpaRepository<Show, Long>, JpaSpecificationExecutor<Show> {
    default Optional<Show> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Show> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Show> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select show from Show show left join fetch show.cantor", countQuery = "select count(show) from Show show")
    Page<Show> findAllWithToOneRelationships(Pageable pageable);

    @Query("select show from Show show left join fetch show.cantor")
    List<Show> findAllWithToOneRelationships();

    @Query("select show from Show show left join fetch show.cantor where show.id =:id")
    Optional<Show> findOneWithToOneRelationships(@Param("id") Long id);
}
