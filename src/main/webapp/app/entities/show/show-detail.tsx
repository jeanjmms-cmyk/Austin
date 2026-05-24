import React, { useEffect } from 'react';
import dayjs from 'dayjs';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './show.reducer';

const dayOfWeekLabels: Record<string, string> = {
  MONDAY: 'Segunda-feira',
  TUESDAY: 'Terca-feira',
  WEDNESDAY: 'Quarta-feira',
  THURSDAY: 'Quinta-feira',
  FRIDAY: 'Sexta-feira',
  SATURDAY: 'Sabado',
  SUNDAY: 'Domingo',
};

const formatShowDate = (value: dayjs.ConfigType, format: string, fallback: string) => {
  const date = dayjs(value);
  return date.isValid() ? date.format(format).replace('.', '') : fallback;
};

const getShowWeekday = (dataShow?: string) => {
  return dataShow ? (dayOfWeekLabels[dataShow] ?? dataShow) : '-';
};

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
              <span>{formatShowDate(showEntity.horarioInicio, 'DD', '--')}</span>
              <small>{formatShowDate(showEntity.horarioInicio, 'MMM', 'Data')}</small>
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
              <h3>Observações</h3>
              <p>{showEntity.observacoes || 'Sem observações cadastradas.'}</p>
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
                <dd>{getShowWeekday(showEntity.dataShow)}</dd>
              </div>
              <div>
                <dt>Data e horário</dt>
                <dd>{formatShowDate(showEntity.horarioInicio, APP_DATE_FORMAT, '-')}</dd>
              </div>
              <div>
                <dt>Valor</dt>
                <dd>{showEntity.valor ? `R$ ${Number(showEntity.valor).toFixed(2).replace('.', ',')}` : '-'}</dd>
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
