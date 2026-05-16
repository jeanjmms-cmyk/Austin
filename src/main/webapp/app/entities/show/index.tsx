import React from 'react';
import { Route } from 'react-router';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Show from './show';
import ShowDetail from './show-detail';
import ShowUpdate from './show-update';
import ShowDeleteDialog from './show-delete-dialog';

const ShowRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Show />} />
    <Route path="new" element={<ShowUpdate />} />
    <Route path=":id">
      <Route index element={<ShowDetail />} />
      <Route path="edit" element={<ShowUpdate />} />
      <Route path="delete" element={<ShowDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ShowRoutes;
