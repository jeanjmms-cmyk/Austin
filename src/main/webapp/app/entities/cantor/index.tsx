import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Cantor from './cantor';
import CantorDetail from './cantor-detail';
import CantorUpdate from './cantor-update';
import CantorDeleteDialog from './cantor-delete-dialog';

const CantorRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Cantor />} />
    <Route path="new" element={<CantorUpdate />} />
    <Route path=":id">
      <Route index element={<CantorDetail />} />
      <Route path="edit" element={<CantorUpdate />} />
      <Route path="delete" element={<CantorDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CantorRoutes;
