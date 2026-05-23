package com.agendashows.service.criteria;

import com.agendashows.domain.enumeration.StatusShow;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agendashows.domain.Show} entity. This class is used
 * in {@link com.agendashows.web.rest.ShowResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shows?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShowCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DayOfWeek
     */
    public static class DayOfWeekFilter extends Filter<DayOfWeek> {

        public DayOfWeekFilter() {}

        public DayOfWeekFilter(DayOfWeekFilter filter) {
            super(filter);
        }

        @Override
        public DayOfWeekFilter copy() {
            return new DayOfWeekFilter(this);
        }
    }

    /**
     * Class for filtering StatusShow
     */
    public static class StatusShowFilter extends Filter<StatusShow> {

        public StatusShowFilter() {}

        public StatusShowFilter(StatusShowFilter filter) {
            super(filter);
        }

        @Override
        public StatusShowFilter copy() {
            return new StatusShowFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter local;

    private DayOfWeekFilter dataShow;

    private InstantFilter horarioInicio;

    private StatusShowFilter status;

    private LongFilter cantorId;

    private Boolean distinct;

    public ShowCriteria() {}

    public ShowCriteria(ShowCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.local = other.optionalLocal().map(StringFilter::copy).orElse(null);
        this.dataShow = other.optionalDataShow().map(DayOfWeekFilter::copy).orElse(null);
        this.horarioInicio = other.optionalHorarioInicio().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StatusShowFilter::copy).orElse(null);
        this.cantorId = other.optionalCantorId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ShowCriteria copy() {
        return new ShowCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLocal() {
        return local;
    }

    public Optional<StringFilter> optionalLocal() {
        return Optional.ofNullable(local);
    }

    public StringFilter local() {
        if (local == null) {
            setLocal(new StringFilter());
        }
        return local;
    }

    public void setLocal(StringFilter local) {
        this.local = local;
    }

    public DayOfWeekFilter getDataShow() {
        return dataShow;
    }

    public Optional<DayOfWeekFilter> optionalDataShow() {
        return Optional.ofNullable(dataShow);
    }

    public DayOfWeekFilter dataShow() {
        if (dataShow == null) {
            setDataShow(new DayOfWeekFilter());
        }
        return dataShow;
    }

    public void setDataShow(DayOfWeekFilter dataShow) {
        this.dataShow = dataShow;
    }

    public InstantFilter getHorarioInicio() {
        return horarioInicio;
    }

    public Optional<InstantFilter> optionalHorarioInicio() {
        return Optional.ofNullable(horarioInicio);
    }

    public InstantFilter horarioInicio() {
        if (horarioInicio == null) {
            setHorarioInicio(new InstantFilter());
        }
        return horarioInicio;
    }

    public void setHorarioInicio(InstantFilter horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public StatusShowFilter getStatus() {
        return status;
    }

    public Optional<StatusShowFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StatusShowFilter status() {
        if (status == null) {
            setStatus(new StatusShowFilter());
        }
        return status;
    }

    public void setStatus(StatusShowFilter status) {
        this.status = status;
    }

    public LongFilter getCantorId() {
        return cantorId;
    }

    public Optional<LongFilter> optionalCantorId() {
        return Optional.ofNullable(cantorId);
    }

    public LongFilter cantorId() {
        if (cantorId == null) {
            setCantorId(new LongFilter());
        }
        return cantorId;
    }

    public void setCantorId(LongFilter cantorId) {
        this.cantorId = cantorId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ShowCriteria that = (ShowCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(local, that.local) &&
            Objects.equals(dataShow, that.dataShow) &&
            Objects.equals(horarioInicio, that.horarioInicio) &&
            Objects.equals(status, that.status) &&
            Objects.equals(cantorId, that.cantorId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, local, dataShow, horarioInicio, status, cantorId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShowCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalLocal().map(f -> "local=" + f + ", ").orElse("") +
            optionalDataShow().map(f -> "dataShow=" + f + ", ").orElse("") +
            optionalHorarioInicio().map(f -> "horarioInicio=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCantorId().map(f -> "cantorId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
