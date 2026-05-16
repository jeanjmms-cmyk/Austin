import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, FormText, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getCantors } from 'app/entities/cantor/cantor.reducer';
import { StatusShow } from 'app/shared/model/enumerations/status-show.model';
import { createEntity, getEntity, reset, updateEntity } from './show.reducer';

export const ShowUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cantors = useAppSelector(state => state.cantor.entities);
  const showEntity = useAppSelector(state => state.show.entity);
  const loading = useAppSelector(state => state.show.loading);
  const updating = useAppSelector(state => state.show.updating);
  const updateSuccess = useAppSelector(state => state.show.updateSuccess);
  const statusShowValues = Object.keys(StatusShow);

  const handleClose = () => {
    navigate(`/show${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCantors({}));
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
    values.horarioInicio = convertDateTimeToServer(values.horarioInicio);

    const entity = {
      ...showEntity,
      ...values,
      cantor: cantors.find(it => it.id.toString() === values.cantor?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          horarioInicio: displayDefaultDateTime(),
        }
      : {
          status: 'AGENDADO',
          ...showEntity,
          horarioInicio: convertDateTimeFromServer(showEntity.horarioInicio),
          cantor: showEntity?.cantor?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="agendaShowsApp.show.home.createOrEditLabel" data-cy="ShowCreateUpdateHeading">
            <Translate contentKey="agendaShowsApp.show.home.createOrEditLabel">Create or edit a Show</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="show-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('agendaShowsApp.show.local')}
                id="show-local"
                name="local"
                data-cy="local"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  minLength: { value: 3, message: translate('entity.validation.minlength', { min: 3 }) },
                  maxLength: { value: 200, message: translate('entity.validation.maxlength', { max: 200 }) },
                }}
              />
              <ValidatedField
                label={translate('agendaShowsApp.show.dataShow')}
                id="show-dataShow"
                name="dataShow"
                data-cy="dataShow"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('agendaShowsApp.show.horarioInicio')}
                id="show-horarioInicio"
                name="horarioInicio"
                data-cy="horarioInicio"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('agendaShowsApp.show.observacoes')}
                id="show-observacoes"
                name="observacoes"
                data-cy="observacoes"
                type="textarea"
              />
              <ValidatedField label={translate('agendaShowsApp.show.status')} id="show-status" name="status" data-cy="status" type="select">
                {statusShowValues.map(statusShow => (
                  <option value={statusShow} key={statusShow}>
                    {translate(`agendaShowsApp.StatusShow.${statusShow}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="show-cantor"
                name="cantor"
                data-cy="cantor"
                label={translate('agendaShowsApp.show.cantor')}
                type="select"
                required
              >
                <option value="" key="0" />
                {cantors
                  ? cantors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.nome}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>
                <Translate contentKey="entity.validation.required">This field is required.</Translate>
              </FormText>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/show" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ShowUpdate;
