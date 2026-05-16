package com.agendashows.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ShowCriteriaTest {

    @Test
    void newShowCriteriaHasAllFiltersNullTest() {
        var showCriteria = new ShowCriteria();
        assertThat(showCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void showCriteriaFluentMethodsCreatesFiltersTest() {
        var showCriteria = new ShowCriteria();

        setAllFilters(showCriteria);

        assertThat(showCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void showCriteriaCopyCreatesNullFilterTest() {
        var showCriteria = new ShowCriteria();
        var copy = showCriteria.copy();

        assertThat(showCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(showCriteria)
        );
    }

    @Test
    void showCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var showCriteria = new ShowCriteria();
        setAllFilters(showCriteria);

        var copy = showCriteria.copy();

        assertThat(showCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(showCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var showCriteria = new ShowCriteria();

        assertThat(showCriteria).hasToString("ShowCriteria{}");
    }

    private static void setAllFilters(ShowCriteria showCriteria) {
        showCriteria.id();
        showCriteria.local();
        showCriteria.dataShow();
        showCriteria.horarioInicio();
        showCriteria.status();
        showCriteria.cantorId();
        showCriteria.distinct();
    }

    private static Condition<ShowCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLocal()) &&
                condition.apply(criteria.getDataShow()) &&
                condition.apply(criteria.getHorarioInicio()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getCantorId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ShowCriteria> copyFiltersAre(ShowCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLocal(), copy.getLocal()) &&
                condition.apply(criteria.getDataShow(), copy.getDataShow()) &&
                condition.apply(criteria.getHorarioInicio(), copy.getHorarioInicio()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getCantorId(), copy.getCantorId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
