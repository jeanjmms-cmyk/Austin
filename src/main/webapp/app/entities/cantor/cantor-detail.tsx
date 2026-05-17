import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cantor.reducer';

const getCantorInitial = (nome?: string) => nome?.trim().charAt(0).toUpperCase() || 'C';

export const CantorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cantorEntity = useAppSelector(state => state.cantor.entity);
  return (
    <Row className="justify-content-center">
      <Col lg="10">
        <section className="cantor-profile" data-cy="cantorDetailsHeading">
          <div className="cantor-profile__hero">
            <div className="cantor-profile__photo-wrap">
              {cantorEntity.fotoPerfil ? (
                <img className="cantor-profile__photo" src={cantorEntity.fotoPerfil} alt={cantorEntity.nome || 'Cantor'} />
              ) : (
                <div className="cantor-profile__avatar">{getCantorInitial(cantorEntity.nome)}</div>
              )}
            </div>
            <div className="cantor-profile__summary">
              <div className="cantor-profile__label">
                <Translate contentKey="agendaShowsApp.cantor.detail.title">Cantor</Translate> #{cantorEntity.id}
              </div>
              <h2 className="cantor-profile__name">{cantorEntity.nome}</h2>
              <div className="cantor-profile__chips">
                <span className="cantor-profile__chip">{cantorEntity.generoMusical || 'Genero nao informado'}</span>
                <span className={`cantor-profile__chip ${cantorEntity.ativo ? 'is-active' : 'is-inactive'}`}>
                  {cantorEntity.ativo ? 'Ativo' : 'Inativo'}
                </span>
              </div>
            </div>
          </div>

          <div className="cantor-profile__content">
            <div className="cantor-profile__section">
              <h3>
                <Translate contentKey="agendaShowsApp.cantor.bio">Bio</Translate>
              </h3>
              <p>{cantorEntity.bio || 'Sem biografia cadastrada.'}</p>
            </div>

            <dl className="cantor-profile__details">
              <div>
                <dt>
                  <Translate contentKey="agendaShowsApp.cantor.nome">Nome</Translate>
                </dt>
                <dd>{cantorEntity.nome || '-'}</dd>
              </div>
              <div>
                <dt>
                  <Translate contentKey="agendaShowsApp.cantor.generoMusical">Genero Musical</Translate>
                </dt>
                <dd>{cantorEntity.generoMusical || '-'}</dd>
              </div>
              <div>
                <dt>
                  <Translate contentKey="agendaShowsApp.cantor.fotoPerfil">Foto Perfil</Translate>
                </dt>
                <dd className="break-word">{cantorEntity.fotoPerfil || '-'}</dd>
              </div>
              <div>
                <dt>
                  <Translate contentKey="agendaShowsApp.cantor.ativo">Ativo</Translate>
                </dt>
                <dd>{cantorEntity.ativo ? 'Ativo' : 'Inativo'}</dd>
              </div>
            </dl>
          </div>

          <div className="cantor-profile__actions">
            <Button tag={Link} to="/cantor" replace color="info" data-cy="entityDetailsBackButton">
              <FontAwesomeIcon icon="arrow-left" />{' '}
              <span>
                <Translate contentKey="entity.action.back">Back</Translate>
              </span>
            </Button>
            <Button tag={Link} to={`/cantor/${cantorEntity.id}/edit`} replace color="primary">
              <FontAwesomeIcon icon="pencil-alt" />{' '}
              <span>
                <Translate contentKey="entity.action.edit">Edit</Translate>
              </span>
            </Button>
          </div>
        </section>
      </Col>
    </Row>
  );
};

export default CantorDetail;
