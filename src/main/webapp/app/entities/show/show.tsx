import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button } from 'reactstrap';
import { JhiItemCount, JhiPagination, TextFormat, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './show.reducer';

const getShowDay = (dataShow?: string) => {
  if (!dataShow) {
    return '--';
  }
  const day = new Date(`${dataShow}T00:00:00`).getDate();
  return Number.isNaN(day) ? '--' : String(day).padStart(2, '0');
};

const getShowMonth = (dataShow?: string) => {
  if (!dataShow) {
    return 'Data';
  }
  const date = new Date(`${dataShow}T00:00:00`);
  return Number.isNaN(date.getTime()) ? 'Data' : date.toLocaleDateString('pt-BR', { month: 'short' }).replace('.', '');
};

export const Show = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const showList = useAppSelector(state => state.show.entities);
  const loading = useAppSelector(state => state.show.loading);
  const totalItems = useAppSelector(state => state.show.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div className="show-page">
      <div className="show-page__header">
        <div>
          <h2 id="show-heading" data-cy="ShowHeading" className="show-page__title">
            <Translate contentKey="agendaShowsApp.show.home.title">Shows</Translate>
          </h2>
          <div className="show-page__meta">
            {totalItems ? (
              <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
            ) : (
              <Translate contentKey="agendaShowsApp.show.home.notFound">No Shows found</Translate>
            )}
          </div>
        </div>
        <div className="show-page__actions">
          <Button className="show-page__action" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="agendaShowsApp.show.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/show/new"
            className="btn btn-primary jh-create-entity show-page__action"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="agendaShowsApp.show.home.createLabel">Create new Show</Translate>
          </Link>
        </div>
      </div>

      <div className="show-sort-bar" aria-label="Ordenar shows">
        {[
          ['id', 'agendaShowsApp.show.id', 'ID'],
          ['local', 'agendaShowsApp.show.local', 'Local'],
          ['dataShow', 'agendaShowsApp.show.dataShow', 'Data Show'],
          ['horarioInicio', 'agendaShowsApp.show.horarioInicio', 'Horario Inicio'],
          ['status', 'agendaShowsApp.show.status', 'Status'],
        ].map(([field, contentKey, label]) => (
          <Button key={field} color="light" size="sm" className="show-sort-bar__button" onClick={sort(field)}>
            <Translate contentKey={contentKey}>{label}</Translate> <FontAwesomeIcon icon={getSortIconByFieldName(field)} />
          </Button>
        ))}
      </div>

      <div>
        {showList && showList.length > 0 ? (
          <div className="show-grid">
            {showList.map((show, i) => (
              <article key={`entity-${i}`} className="show-card" data-cy="entityTable">
                <div className="show-card__media">
                  <div className="show-card__date">
                    <span>{getShowDay(show.dataShow)}</span>
                    <small>{getShowMonth(show.dataShow)}</small>
                  </div>
                  <span className="show-card__status">
                    <Translate contentKey={`agendaShowsApp.StatusShow.${show.status}`} />
                  </span>
                </div>
                <div className="show-card__body">
                  <div className="show-card__eyebrow">#{show.id}</div>
                  <h3 className="show-card__local">{show.local}</h3>
                  <div className="show-card__info">
                    {show.dataShow ? <TextFormat type="date" value={show.dataShow} format={APP_LOCAL_DATE_FORMAT} /> : 'Data nao informada'}
                  </div>
                  <div className="show-card__info">
                    {show.horarioInicio ? (
                      <TextFormat type="date" value={show.horarioInicio} format={APP_DATE_FORMAT} />
                    ) : (
                      'Horario nao informado'
                    )}
                  </div>
                  <div className="show-card__artist">
                    {show.cantor ? <Link to={`/cantor/${show.cantor.id}`}>{show.cantor.nome}</Link> : 'Cantor nao informado'}
                  </div>
                  <p className="show-card__notes">{show.observacoes || 'Sem observacoes cadastradas.'}</p>
                </div>
                <div className="show-card__actions">
                  <Button tag={Link} to={`/show/${show.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                    <FontAwesomeIcon icon="eye" />{' '}
                    <span>
                      <Translate contentKey="entity.action.view">View</Translate>
                    </span>
                  </Button>
                  <Button
                    tag={Link}
                    to={`/show/${show.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                    color="primary"
                    size="sm"
                    data-cy="entityEditButton"
                  >
                    <FontAwesomeIcon icon="pencil-alt" />{' '}
                    <span>
                      <Translate contentKey="entity.action.edit">Edit</Translate>
                    </span>
                  </Button>
                  <Button
                    onClick={() =>
                      (window.location.href = `/show/${show.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                    }
                    color="danger"
                    size="sm"
                    data-cy="entityDeleteButton"
                  >
                    <FontAwesomeIcon icon="trash" />{' '}
                    <span>
                      <Translate contentKey="entity.action.delete">Delete</Translate>
                    </span>
                  </Button>
                </div>
              </article>
            ))}
          </div>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="agendaShowsApp.show.home.notFound">No Shows found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={showList && showList.length > 0 ? '' : 'd-none'}>
          <div className="show-pagination">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Show;
