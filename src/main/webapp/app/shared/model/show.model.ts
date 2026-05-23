import dayjs from 'dayjs';
import { ICantor } from 'app/shared/model/cantor.model';
import { StatusShow } from 'app/shared/model/enumerations/status-show.model';

export interface IShow {
  id?: number;
  local?: string;
  dataShow?: string;
  horarioInicio?: dayjs.Dayjs;
  observacoes?: string | null;
  status?: keyof typeof StatusShow;
  cantor?: ICantor;
}

export const defaultValue: Readonly<IShow> = {};
