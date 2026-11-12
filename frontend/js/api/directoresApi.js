import { request } from './httpClient.js';

export const directoresApi = {
  getAll: () => request('/directores'),
  getById: (id) => request(`/directores/${id}`),
  create: (payload) =>
    request('/directores', { method: 'POST', body: JSON.stringify(payload) }),
  update: (id, payload) =>
    request(`/directores/${id}`, { method: 'PUT', body: JSON.stringify(payload) }),
  remove: (id) => request(`/directores/${id}`, { method: 'DELETE' }),
};

