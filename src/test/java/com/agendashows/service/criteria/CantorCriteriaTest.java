package com.agendashows.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CantorCriteriaTest {

    @Test
    void newCantorCriteriaHasAllFiltersNullTest() {
        var cantorCriteria = new CantorCriteria();
        assertThat(cantorCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cantorCriteriaFluentMethodsCreatesFiltersTest() {
        var cantorCriteria = new CantorCriteria();

        setAllFilters(cantorCriteria);

        assertThat(cantorCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cantorCriteriaCopyCreatesNullFilterTest() {
        var cantorCriteria = new CantorCriteria();
        var copy = cantorCriteria.copy();

        assertThat(cantorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cantorCriteria)
        );
    }

    @Test
    void cantorCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cantorCriteria = new CantorCriteria();
        setAllFilters(cantorCriteria);

        var copy = cantorCriteria.copy();

        assertThat(cantorCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cantorCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cantorCriteria = new CantorCriteria();

        assertThat(cantorCriteria).hasToString("CantorCriteria{}");
    }

    private static void setAllFilters(CantorCriteria cantorCriteria) {
        cantorCriteria.id();
        cantorCriteria.nome();
        cantorCriteria.generoMusical();
        cantorCriteria.fotoPerfil();
        cantorCriteria.ativo();
        cantorCriteria.distinct();
    }

    private static Condition<CantorCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNome()) &&
                condition.apply(criteria.getGeneroMusical()) &&
                condition.apply(criteria.getFotoPerfil()) &&
                condition.apply(criteria.getAtivo()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CantorCriteria> copyFiltersAre(CantorCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNome(), copy.getNome()) &&
                condition.apply(criteria.getGeneroMusical(), copy.getGeneroMusical()) &&
                condition.apply(criteria.getFotoPerfil(), copy.getFotoPerfil()) &&
                condition.apply(criteria.getAtivo(), copy.getAtivo()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
