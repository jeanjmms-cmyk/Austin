import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMicrophone } from '@fortawesome/free-solid-svg-icons';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './cantor.reducer';
import { defaultValue } from 'app/shared/model/cantor.model';

export const CantorUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cantorEntity = useAppSelector(state => state.cantor.entity);
  const loading = useAppSelector(state => state.cantor.loading);
  const updating = useAppSelector(state => state.cantor.updating);
  const updateSuccess = useAppSelector(state => state.cantor.updateSuccess);

  const handleClose = () => {
    navigate(`/cantor${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...cantorEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? defaultValue
      : {
          ...cantorEntity,
        };

  return (
    <div className="cantor-form-page">
      <Row className="justify-content-center">
        <Col lg="8">
          <div className="cantor-form-page__header">
            <div className="cantor-form-page__icon">
              <FontAwesomeIcon icon={faMicrophone} />
            </div>
            <h2 id="agendaShowsApp.cantor.home.createOrEditLabel" data-cy="CantorCreateUpdateHeading">
              <Translate contentKey="agendaShowsApp.cantor.home.createOrEditLabel">Create or edit an Artist</Translate>
            </h2>
          </div>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col lg="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm className="cantor-form" defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="cantor-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('agendaShowsApp.cantor.nome')}
                id="cantor-nome"
                name="nome"
                data-cy="nome"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 2, message: translate('entity.validation.minlength', { min: 2 }) },
                  maxLength: { value: 100, message: translate('entity.validation.maxlength', { max: 100 }) },
                }}
              />
              <ValidatedField
                className="cantor-form__field"
                label={translate('agendaShowsApp.cantor.generoMusical')}
                id="cantor-generoMusical"
                name="generoMusical"
                data-cy="generoMusical"
                type="text"
                validate={{
                  maxLength: { value: 60, message: translate('entity.validation.maxlength', { max: 60 }) },
                }}
              />
              <ValidatedField
                className="cantor-form__field"
                label={translate('agendaShowsApp.cantor.fotoPerfil')}
                id="cantor-fotoPerfil"
                name="fotoPerfil"
                data-cy="fotoPerfil"
                type="text"
                validate={{
                  maxLength: { value: 255, message: translate('entity.validation.maxlength', { max: 255 }) },
                }}
              />
              <ValidatedField label={translate('agendaShowsApp.cantor.bio')} id="cantor-bio" name="bio" data-cy="bio" type="textarea" />
              <ValidatedField
                className="cantor-form__active"
                label={'Disponibilidade'}
                id="cantor-ativo"
                name="ativo"
                data-cy="ativo"
                check
                type="checkbox"
              />
              <div className="cantor-form__actions">
                <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cantor" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span>
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </div>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CantorUpdate;
