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
    <Row className="justify-content-center">
      <Col lg="10">
        <section className="show-profile" data-cy="showDetailsHeading">
          <div className="show-profile__hero">
            <div className="show-profile__calendar">
              <span>{showEntity.horarioInicio ? <TextFormat value={showEntity.horarioInicio} type="date" format="DD" /> : '--'}</span>
              <small>{showEntity.horarioInicio ? <TextFormat value={showEntity.horarioInicio} type="date" format="MMM" /> : 'Data'}</small>
            </div>
            <div className="show-profile__summary">
              <div className="show-profile__label">
                <Translate contentKey="agendaShowsApp.show.detail.title">Show</Translate> #{showEntity.id}
              </div>
              <h2 className="show-profile__name">{showEntity.local}</h2>
              <div className="show-profile__chips">
                <span className="show-profile__chip">
                  {showEntity.status ? <Translate contentKey={`agendaShowsApp.StatusShow.${showEntity.status}`} /> : '-'}
                </span>
                <span className="show-profile__chip">{showEntity.cantor ? showEntity.cantor.nome : 'Cantor nao informado'}</span>
              </div>
            </div>
          </div>

          <div className="show-profile__content">
            <div className="show-profile__section">
              <h3>
                <Translate contentKey="agendaShowsApp.show.observacoes">Observacoes</Translate>
              </h3>
              <p>{showEntity.observacoes || 'Sem observacoes cadastradas.'}</p>
            </div>

            <dl className="show-profile__details">
              <div>
                <dt>
                  <Translate contentKey="agendaShowsApp.show.local">Local</Translate>
                </dt>
                <dd>{showEntity.local || '-'}</dd>
              </div>
              <div>
                <dt>
                  <Translate contentKey="agendaShowsApp.show.dataShow">Data Show</Translate>
                </dt>
                <dd>
                  {showEntity.horarioInicio ? (
                    <TextFormat value={showEntity.horarioInicio} type="date" format={APP_LOCAL_DATE_FORMAT} />
                  ) : (
                    '-'
                  )}
                </dd>
              </div>
              <div>
                <dt>
                  <Translate contentKey="agendaShowsApp.show.horarioInicio">Horario Inicio</Translate>
                </dt>
                <dd>
                  {showEntity.horarioInicio ? <TextFormat value={showEntity.horarioInicio} type="date" format={APP_DATE_FORMAT} /> : '-'}
                </dd>
              </div>
              <div>
                <dt>
                  <Translate contentKey="agendaShowsApp.show.cantor">Cantor</Translate>
                </dt>
                <dd>{showEntity.cantor ? showEntity.cantor.nome : '-'}</dd>
              </div>
            </dl>
          </div>

          <div className="show-profile__actions">
            <Button tag={Link} to="/show" replace color="info" data-cy="entityDetailsBackButton">
              <FontAwesomeIcon icon="arrow-left" />{' '}
              <span>
                <Translate contentKey="entity.action.back">Back</Translate>
              </span>
            </Button>
            <Button tag={Link} to={`/show/${showEntity.id}/edit`} replace color="primary">
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

export default ShowDetail;
