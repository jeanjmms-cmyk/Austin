package com.agendashows.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.agendashows.domain.Cantor} entity. This class is used
 * in {@link com.agendashows.web.rest.CantorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cantors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CantorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter nome;

    private StringFilter generoMusical;

    private StringFilter fotoPerfil;

    private BooleanFilter ativo;

    private Boolean distinct;

    public CantorCriteria() {}

    public CantorCriteria(CantorCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.nome = other.optionalNome().map(StringFilter::copy).orElse(null);
        this.generoMusical = other.optionalGeneroMusical().map(StringFilter::copy).orElse(null);
        this.fotoPerfil = other.optionalFotoPerfil().map(StringFilter::copy).orElse(null);
        this.ativo = other.optionalAtivo().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CantorCriteria copy() {
        return new CantorCriteria(this);
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

    public StringFilter getNome() {
        return nome;
    }

    public Optional<StringFilter> optionalNome() {
        return Optional.ofNullable(nome);
    }

    public StringFilter nome() {
        if (nome == null) {
            setNome(new StringFilter());
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getGeneroMusical() {
        return generoMusical;
    }

    public Optional<StringFilter> optionalGeneroMusical() {
        return Optional.ofNullable(generoMusical);
    }

    public StringFilter generoMusical() {
        if (generoMusical == null) {
            setGeneroMusical(new StringFilter());
        }
        return generoMusical;
    }

    public void setGeneroMusical(StringFilter generoMusical) {
        this.generoMusical = generoMusical;
    }

    public StringFilter getFotoPerfil() {
        return fotoPerfil;
    }

    public Optional<StringFilter> optionalFotoPerfil() {
        return Optional.ofNullable(fotoPerfil);
    }

    public StringFilter fotoPerfil() {
        if (fotoPerfil == null) {
            setFotoPerfil(new StringFilter());
        }
        return fotoPerfil;
    }

    public void setFotoPerfil(StringFilter fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public BooleanFilter getAtivo() {
        return ativo;
    }

    public Optional<BooleanFilter> optionalAtivo() {
        return Optional.ofNullable(ativo);
    }

    public BooleanFilter ativo() {
        if (ativo == null) {
            setAtivo(new BooleanFilter());
        }
        return ativo;
    }

    public void setAtivo(BooleanFilter ativo) {
        this.ativo = ativo;
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
        final CantorCriteria that = (CantorCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(nome, that.nome) &&
            Objects.equals(generoMusical, that.generoMusical) &&
            Objects.equals(fotoPerfil, that.fotoPerfil) &&
            Objects.equals(ativo, that.ativo) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, generoMusical, fotoPerfil, ativo, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CantorCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalNome().map(f -> "nome=" + f + ", ").orElse("") +
            optionalGeneroMusical().map(f -> "generoMusical=" + f + ", ").orElse("") +
            optionalFotoPerfil().map(f -> "fotoPerfil=" + f + ", ").orElse("") +
            optionalAtivo().map(f -> "ativo=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
