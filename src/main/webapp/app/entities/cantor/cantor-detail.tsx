import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cantor.reducer';

export const CantorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cantorEntity = useAppSelector(state => state.cantor.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cantorDetailsHeading">
          <Translate contentKey="agendaShowsApp.cantor.detail.title">Cantor</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cantorEntity.id}</dd>
          <dt>
            <span id="nome">
              <Translate contentKey="agendaShowsApp.cantor.nome">Nome</Translate>
            </span>
          </dt>
          <dd>{cantorEntity.nome}</dd>
          <dt>
            <span id="generoMusical">
              <Translate contentKey="agendaShowsApp.cantor.generoMusical">Genero Musical</Translate>
            </span>
          </dt>
          <dd>{cantorEntity.generoMusical}</dd>
          <dt>
            <span id="bio">
              <Translate contentKey="agendaShowsApp.cantor.bio">Bio</Translate>
            </span>
          </dt>
          <dd>{cantorEntity.bio}</dd>
          <dt>
            <span id="fotoPerfil">
              <Translate contentKey="agendaShowsApp.cantor.fotoPerfil">Foto Perfil</Translate>
            </span>
          </dt>
          <dd>{cantorEntity.fotoPerfil}</dd>
          <dt>
            <span id="ativo">
              <Translate contentKey="agendaShowsApp.cantor.ativo">Ativo</Translate>
            </span>
          </dt>
          <dd>{cantorEntity.ativo ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/cantor" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cantor/${cantorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CantorDetail;
