import './home.scss';

import React, { useEffect, useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Alert, Col, Row } from 'reactstrap';
import dayjs from 'dayjs';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities } from 'app/entities/show/show.reducer';

export const Home = () => {
  const dispatch = useAppDispatch();
  const account = useAppSelector(state => state.authentication.account);
  const showList = useAppSelector(state => state.show.entities);

  const [mesSelecionado, setMesSelecionado] = useState('');
  const [localSelecionado, setLocalSelecionado] = useState('');

  useEffect(() => {
    dispatch(getEntities({ page: 0, size: 1000, sort: 'horarioInicio,asc' }));
  }, []);

  // Mês seguinte como padrão inicial
  useEffect(() => {
    const proximoMes = dayjs().add(1, 'month').format('YYYY-MM');
    setMesSelecionado(proximoMes);
  }, []);

  const mesesDisponiveis = useMemo(() => {
    const set = new Set<string>();
    showList.forEach(show => {
      if (show.horarioInicio) {
        set.add(dayjs(show.horarioInicio).format('YYYY-MM'));
      }
    });
    return Array.from(set).sort();
  }, [showList]);

  const locaisDisponiveis = useMemo(() => {
    const set = new Set<string>();
    showList.forEach(show => {
      if (show.local) set.add(show.local);
    });
    return Array.from(set).sort();
  }, [showList]);

  const showsFiltrados = useMemo(() => {
    return showList.filter(show => {
      const mesShow = show.horarioInicio ? dayjs(show.horarioInicio).format('YYYY-MM') : '';
      const porMes = !mesSelecionado || mesShow === mesSelecionado;
      const porLocal = !localSelecionado || show.local === localSelecionado;
      return porMes && porLocal;
    });
  }, [showList, mesSelecionado, localSelecionado]);

  const receitaPorCantor = useMemo(() => {
    const mapa: Record<string, { nome: string; receita: number }> = {};
    showsFiltrados.forEach(show => {
      const nome = show.cantor?.nome ?? 'Cantor não informado';
      if (!mapa[nome]) mapa[nome] = { nome, receita: 0 };
      mapa[nome].receita += Number(show.valor ?? 0);
    });
    return Object.values(mapa).sort((a, b) => b.receita - a.receita);
  }, [showsFiltrados]);

  const totalReceita = receitaPorCantor.reduce((acc, c) => acc + c.receita, 0);

  const fmt = (n: number) => n.toLocaleString('pt-BR', { style: 'currency', currency: 'BRL' });

  const labelMes = (yyyymm: string) => dayjs(yyyymm + '-01').format('MMMM [de] YYYY');

  return (
    <Row>
      <Col md="10">
        {account?.login ? (
          <div>
            <div className="title-home">
              <h3 className="show-page__title">Sistema de Shows Austin Produções</h3>
            </div>
            <div className="financial-table">
              <div className="financial-table__header">
                <h2 className="financial-table__title">Receita</h2>
                <div className="financial-table__filters">
                  <select value={mesSelecionado} onChange={e => setMesSelecionado(e.target.value)} className="financial-table__select">
                    <option value="">Todos os meses</option>
                    {mesesDisponiveis.map(m => (
                      <option key={m} value={m}>
                        {labelMes(m)}
                      </option>
                    ))}
                  </select>
                  <select value={localSelecionado} onChange={e => setLocalSelecionado(e.target.value)} className="financial-table__select">
                    <option value="">Todos os locais</option>
                    {locaisDisponiveis.map(l => (
                      <option key={l} value={l}>
                        {l}
                      </option>
                    ))}
                  </select>
                </div>
              </div>
              {receitaPorCantor.length > 0 ? (
                <table className="financial-table__table">
                  <thead>
                    <tr>
                      <th>Cantor</th>
                      <th>Receita gerada</th>
                    </tr>
                  </thead>
                  <tbody>
                    {receitaPorCantor.map(c => (
                      <tr key={c.nome}>
                        <td>{c.nome}</td>
                        <td>{fmt(c.receita)}</td>
                      </tr>
                    ))}
                  </tbody>
                  <tfoot>
                    <tr>
                      <td>Total</td>
                      <td>{fmt(totalReceita)}</td>
                    </tr>
                  </tfoot>
                </table>
              ) : (
                <p className="financial-table__empty">Nenhum show encontrado para os filtros selecionados.</p>
              )}
            </div>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>
              <Link to="/login" className="alert-link">
                <Translate contentKey="global.messages.info.authenticated.link"> sign in</Translate>
              </Link>
            </Alert>
            <Alert color="warning">
              <Translate contentKey="global.messages.info.register.noaccount">You do not have an account yet?</Translate>&nbsp;
              <Link to="/account/register" className="alert-link">
                <Translate contentKey="global.messages.info.register.link">Register a new account</Translate>
              </Link>
            </Alert>
          </div>
        )}
      </Col>
    </Row>
  );
};

export default Home;
