import { request } from './httpClient.js';

export const productosApi = {
  getAll: () => request('/productos'),
  getTipos: () => request('/productos/tipos'),
  getBySemilleroId: (semilleroId) => request(`/productos/semillero/${semilleroId}`),
  getById: (id) => request(`/productos/${id}`),
  create: (payload) =>
    request('/productos', { method: 'POST', body: JSON.stringify(payload) }),
  update: (id, payload) =>
    request(`/productos/${id}`, { method: 'PUT', body: JSON.stringify(payload) }),
  remove: (id) => request(`/productos/${id}`, { method: 'DELETE' }),
};

