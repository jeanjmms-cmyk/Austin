package com.agendashows.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.agendashows.domain.Cantor} entity.
 */
@Schema(description = "Cantor / Artista")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CantorDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    private String nome;

    @Size(max = 60)
    private String generoMusical;

    @Lob
    private String bio;

    @Size(max = 255)
    private String fotoPerfil;

    @NotNull
    private Boolean ativo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGeneroMusical() {
        return generoMusical;
    }

    public void setGeneroMusical(String generoMusical) {
        this.generoMusical = generoMusical;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CantorDTO)) {
            return false;
        }

        CantorDTO cantorDTO = (CantorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, cantorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CantorDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", generoMusical='" + getGeneroMusical() + "'" +
            ", bio='" + getBio() + "'" +
            ", fotoPerfil='" + getFotoPerfil() + "'" +
            ", ativo='" + getAtivo() + "'" +
            "}";
    }
}
