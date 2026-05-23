import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button } from 'reactstrap';
import { JhiItemCount, JhiPagination, Translate, getPaginationState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './cantor.reducer';

const getCantorInitial = (nome?: string) => nome?.trim().charAt(0).toUpperCase() || 'C';

export const Cantor = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const cantorList = useAppSelector(state => state.cantor.entities);
  const loading = useAppSelector(state => state.cantor.loading);
  const totalItems = useAppSelector(state => state.cantor.totalItems);

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
    <div className="cantor-page">
      <div className="cantor-page__header">
        <div>
          <h2 id="cantor-heading" data-cy="CantorHeading" className="cantor-page__title">
            Artistas
          </h2>
          <div className="cantor-page__meta">
            {totalItems ? (
              <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
            ) : (
              <Translate contentKey="agendaShowsApp.cantor.home.notFound">No Cantors found</Translate>
            )}
          </div>
        </div>
        <div className="cantor-page__actions">
          <Button className="cantor-page__action" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="agendaShowsApp.cantor.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/cantor/new"
            className="btn btn-primary jh-create-entity cantor-page__action"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="agendaShowsApp.cantor.home.createLabel">Create new Cantor</Translate>
          </Link>
        </div>
      </div>

      <div className="cantor-sort-bar" aria-label="Ordenar cantores">
        {[
          ['id', 'agendaShowsApp.cantor.id', 'ID'],
          ['nome', 'agendaShowsApp.cantor.nome', 'Nome'],
          ['generoMusical', 'agendaShowsApp.cantor.generoMusical', 'Genero Musical'],
          ['ativo', 'agendaShowsApp.cantor.ativo', 'Ativo'],
        ].map(([field, contentKey, label]) => (
          <Button key={field} color="light" size="sm" className="cantor-sort-bar__button" onClick={sort(field)}>
            <Translate contentKey={contentKey}>{label}</Translate> <FontAwesomeIcon icon={getSortIconByFieldName(field)} />
          </Button>
        ))}
      </div>

      <div>
        {cantorList && cantorList.length > 0 ? (
          <div className="cantor-grid">
            {cantorList.map((cantor, i) => (
              <article key={`entity-${i}`} className="cantor-card" data-cy="entityTable">
                <div className="cantor-card__media">
                  {cantor.fotoPerfil ? (
                    <img className="cantor-card__photo" src={cantor.fotoPerfil} alt={cantor.nome || 'Cantor'} />
                  ) : (
                    <div className="cantor-card__avatar">{getCantorInitial(cantor.nome)}</div>
                  )}
                  <span className={`cantor-card__status ${cantor.ativo ? 'is-active' : 'is-inactive'}`}>
                    {cantor.ativo ? 'Ativo' : 'Inativo'}
                  </span>
                </div>
                <div className="cantor-card__body">
                  <div className="cantor-card__eyebrow">#{cantor.id}</div>
                  <h3 className="cantor-card__name">{cantor.nome}</h3>
                  <div className="cantor-card__genre">{cantor.generoMusical || 'Genero nao informado'}</div>
                  <p className="cantor-card__bio">{cantor.bio || 'Sem biografia cadastrada.'}</p>
                </div>
                <div className="cantor-card__actions">
                  <Button tag={Link} to={`/cantor/${cantor.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                    <FontAwesomeIcon icon="eye" />{' '}
                    <span>
                      <Translate contentKey="entity.action.view">View</Translate>
                    </span>
                  </Button>
                  <Button
                    tag={Link}
                    to={`/cantor/${cantor.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                      (window.location.href = `/cantor/${cantor.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="agendaShowsApp.cantor.home.notFound">No Cantors found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={cantorList && cantorList.length > 0 ? '' : 'd-none'}>
          <div className="cantor-pagination">
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

export default Cantor;
