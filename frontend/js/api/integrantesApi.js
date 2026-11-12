import { request } from './httpClient.js';

export const integrantesApi = {
  getAll: () => request('/integrantes'),
  getBySemilleroId: (semilleroId) => request(`/integrantes/semillero/${semilleroId}`),
  getById: (id) => request(`/integrantes/${id}`),
  create: (payload) =>
    request('/integrantes', { method: 'POST', body: JSON.stringify(payload) }),
  update: (id, payload) =>
    request(`/integrantes/${id}`, { method: 'PUT', body: JSON.stringify(payload) }),
  remove: (id) => request(`/integrantes/${id}`, { method: 'DELETE' }),
};

