import { request } from './httpClient.js';

export const lineasApi = {
  getAll: () => request('/lineas'),
  getById: (id) => request(`/lineas/${id}`),
  create: (payload) =>
    request('/lineas', { method: 'POST', body: JSON.stringify(payload) }),
  update: (id, payload) =>
    request(`/lineas/${id}`, { method: 'PUT', body: JSON.stringify(payload) }),
  remove: (id) => request(`/lineas/${id}`, { method: 'DELETE' }),
};

