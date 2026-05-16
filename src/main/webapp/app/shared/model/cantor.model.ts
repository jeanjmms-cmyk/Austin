export interface ICantor {
  id?: number;
  nome?: string;
  generoMusical?: string | null;
  bio?: string | null;
  fotoPerfil?: string | null;
  ativo?: boolean;
}

export const defaultValue: Readonly<ICantor> = {
  ativo: false,
};
