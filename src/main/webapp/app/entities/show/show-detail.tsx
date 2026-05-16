import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './show.reducer';

export const ShowDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const showEntity = useAppSelector(state => state.show.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="showDetailsHeading">
          <Translate contentKey="agendaShowsApp.show.detail.title">Show</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{showEntity.id}</dd>
          <dt>
            <span id="local">
              <Translate contentKey="agendaShowsApp.show.local">Local</Translate>
            </span>
          </dt>
          <dd>{showEntity.local}</dd>
          <dt>
            <span id="dataShow">
              <Translate contentKey="agendaShowsApp.show.dataShow">Data Show</Translate>
            </span>
          </dt>
          <dd>{showEntity.dataShow ? <TextFormat value={showEntity.dataShow} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="horarioInicio">
              <Translate contentKey="agendaShowsApp.show.horarioInicio">Horario Inicio</Translate>
            </span>
          </dt>
          <dd>{showEntity.horarioInicio ? <TextFormat value={showEntity.horarioInicio} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="observacoes">
              <Translate contentKey="agendaShowsApp.show.observacoes">Observacoes</Translate>
            </span>
          </dt>
          <dd>{showEntity.observacoes}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="agendaShowsApp.show.status">Status</Translate>
            </span>
          </dt>
          <dd>{showEntity.status}</dd>
          <dt>
            <Translate contentKey="agendaShowsApp.show.cantor">Cantor</Translate>
          </dt>
          <dd>{showEntity.cantor ? showEntity.cantor.nome : ''}</dd>
        </dl>
        <Button tag={Link} to="/show" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/show/${showEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ShowDetail;
