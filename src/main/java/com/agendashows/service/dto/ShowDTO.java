package com.agendashows.service.dto;

import com.agendashows.domain.enumeration.StatusShow;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.agendashows.domain.Show} entity.
 */
@Schema(description = "Show / Apresentação")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShowDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 200)
    private String local;

    @NotNull
    private LocalDate dataShow;

    @NotNull
    private Instant horarioInicio;

    @Lob
    private String observacoes;

    @NotNull
    private StatusShow status;

    @NotNull
    private CantorDTO cantor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public LocalDate getDataShow() {
        return dataShow;
    }

    public void setDataShow(LocalDate dataShow) {
        this.dataShow = dataShow;
    }

    public Instant getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(Instant horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public StatusShow getStatus() {
        return status;
    }

    public void setStatus(StatusShow status) {
        this.status = status;
    }

    public CantorDTO getCantor() {
        return cantor;
    }

    public void setCantor(CantorDTO cantor) {
        this.cantor = cantor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShowDTO)) {
            return false;
        }

        ShowDTO showDTO = (ShowDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, showDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShowDTO{" +
            "id=" + getId() +
            ", local='" + getLocal() + "'" +
            ", dataShow='" + getDataShow() + "'" +
            ", horarioInicio='" + getHorarioInicio() + "'" +
            ", observacoes='" + getObservacoes() + "'" +
            ", status='" + getStatus() + "'" +
            ", cantor=" + getCantor() +
            "}";
    }
}
