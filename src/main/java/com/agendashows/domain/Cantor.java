package com.agendashows.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Cantor / Artista
 */
@Entity
@Table(name = "cantor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Cantor implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(name = "nome", length = 100, nullable = false)
    private String nome;

    @Size(max = 60)
    @Column(name = "genero_musical", length = 60)
    private String generoMusical;

    @Lob
    @Column(name = "bio")
    private String bio;

    @Size(max = 255)
    @Column(name = "foto_perfil", length = 255)
    private String fotoPerfil;

    @NotNull
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Cantor id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Cantor nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGeneroMusical() {
        return this.generoMusical;
    }

    public Cantor generoMusical(String generoMusical) {
        this.setGeneroMusical(generoMusical);
        return this;
    }

    public void setGeneroMusical(String generoMusical) {
        this.generoMusical = generoMusical;
    }

    public String getBio() {
        return this.bio;
    }

    public Cantor bio(String bio) {
        this.setBio(bio);
        return this;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFotoPerfil() {
        return this.fotoPerfil;
    }

    public Cantor fotoPerfil(String fotoPerfil) {
        this.setFotoPerfil(fotoPerfil);
        return this;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public Cantor ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cantor)) {
            return false;
        }
        return getId() != null && getId().equals(((Cantor) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Cantor{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", generoMusical='" + getGeneroMusical() + "'" +
            ", bio='" + getBio() + "'" +
            ", fotoPerfil='" + getFotoPerfil() + "'" +
            ", ativo='" + getAtivo() + "'" +
            "}";
    }
}
