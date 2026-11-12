import { request } from './httpClient.js';

export const semillerosApi = {
  getAll: () => request('/semilleros'),
  getById: (id) => request(`/semilleros/${id}`),
  create: (payload) =>
    request('/semilleros', { method: 'POST', body: JSON.stringify(payload) }),
  update: (id, payload) =>
    request(`/semilleros/${id}`, { method: 'PUT', body: JSON.stringify(payload) }),
  remove: (id) => request(`/semilleros/${id}`, { method: 'DELETE' }),
  addLinea: (semilleroId, lineaId) =>
    request(`/semilleros/${semilleroId}/lineas/${lineaId}`, { method: 'POST' }),
  removeLinea: (semilleroId, lineaId) =>
    request(`/semilleros/${semilleroId}/lineas/${lineaId}`, { method: 'DELETE' }),
};

