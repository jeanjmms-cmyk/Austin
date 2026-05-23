package com.agendashows.domain;

import com.agendashows.domain.enumeration.StatusShow;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Show / Apresentação
 */
@Entity
@Table(name = "show")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Show implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 200)
    @Column(name = "local", length = 200, nullable = false)
    private String local;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "data_show", nullable = false)
    private DayOfWeek dataShow;

    @NotNull
    @Column(name = "horario_inicio", nullable = false)
    private Instant horarioInicio;

    @Lob
    @Column(name = "observacoes")
    private String observacoes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusShow status;

    @ManyToOne(optional = false)
    @NotNull
    private Cantor cantor;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Show id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocal() {
        return this.local;
    }

    public Show local(String local) {
        this.setLocal(local);
        return this;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public DayOfWeek getDataShow() {
        return this.dataShow;
    }

    public Show dataShow(DayOfWeek dataShow) {
        this.setDataShow(dataShow);
        return this;
    }

    public void setDataShow(DayOfWeek dataShow) {
        this.dataShow = dataShow;
    }

    public Instant getHorarioInicio() {
        return this.horarioInicio;
    }

    public Show horarioInicio(Instant horarioInicio) {
        this.setHorarioInicio(horarioInicio);
        return this;
    }

    public void setHorarioInicio(Instant horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getObservacoes() {
        return this.observacoes;
    }

    public Show observacoes(String observacoes) {
        this.setObservacoes(observacoes);
        return this;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public StatusShow getStatus() {
        return this.status;
    }

    public Show status(StatusShow status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusShow status) {
        this.status = status;
    }

    public Cantor getCantor() {
        return this.cantor;
    }

    public void setCantor(Cantor cantor) {
        this.cantor = cantor;
    }

    public Show cantor(Cantor cantor) {
        this.setCantor(cantor);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Show)) {
            return false;
        }
        return getId() != null && getId().equals(((Show) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Show{" +
            "id=" + getId() +
            ", local='" + getLocal() + "'" +
            ", dataShow='" + getDataShow() + "'" +
            ", horarioInicio='" + getHorarioInicio() + "'" +
            ", observacoes='" + getObservacoes() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
