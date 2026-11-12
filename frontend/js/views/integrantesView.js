import { store, loadAllData, getSemilleroById } from '../state/store.js';
import { integrantesApi } from '../api/integrantesApi.js';
import { confirmDialog } from '../ui/modal.js';
import { showToast } from '../ui/toast.js';
import {
  escapeHtml,
  getInitials,
  openIntegranteFormModal,
  openSemilleroPreviewModal,
  renderEmptyState,
} from '../ui/components.js';

let filters = {
  query: '',
  semilleroId: '',
  soloLideres: false,
};

export function renderIntegrantesView(container) {
  const { integrantes, semilleros } = store;

  const filtered = integrantes.filter((i) => {
    const q = filters.query.trim().toLowerCase();
    const matchesQuery =
      !q ||
      i.nombre.toLowerCase().includes(q) ||
      i.codigo.toLowerCase().includes(q) ||
      i.programa.toLowerCase().includes(q) ||
      (i.semilleroNombre || '').toLowerCase().includes(q) ||
      (i.semilleroSigla || '').toLowerCase().includes(q);

    const matchesSemillero =
      !filters.semilleroId || Number(i.semilleroId) === Number(filters.semilleroId);

    const matchesLider = !filters.soloLideres || Boolean(i.esLider);

    return matchesQuery && matchesSemillero && matchesLider;
  });

  const semilleroOptions = semilleros
    .map(
      (s) =>
        `<option value="${s.id}" ${Number(filters.semilleroId) === Number(s.id) ? 'selected' : ''}>${escapeHtml(s.sigla)} — ${escapeHtml(s.nombre)}</option>`
    )
    .join('');

  const tableHtml = filtered.length
    ? `
      <div class="table-card">
        <div class="table-responsive">
          <table class="data-table">
            <thead>
              <tr>
                <th>Integrante</th>
                <th>Código</th>
                <th>Programa Académico</th>
                <th>Semillero (clic para ver info)</th>
                <th>Rol</th>
                <th style="text-align: right;">Acciones</th>
              </tr>
            </thead>
            <tbody>
              ${filtered
                .map(
                  (i) => `
                <tr>
                  <td>
                    <div class="person-cell">
                      <div class="avatar ${i.esLider ? 'avatar--leader' : ''}">${escapeHtml(getInitials(i.nombre))}</div>
                      <strong>${escapeHtml(i.nombre)}</strong>
                    </div>
                  </td>
                  <td><code>${escapeHtml(i.codigo)}</code></td>
                  <td>${escapeHtml(i.programa)}</td>
                  <td>
                    <button type="button" class="semillero-link-btn" data-view-semillero="${i.semilleroId}" data-integrante-id="${i.id}" title="Ver información del semillero">
                      <span>${escapeHtml(i.semilleroSigla)}</span>
                      <span class="text-muted" style="font-weight: 400;">· Ver info</span>
                    </button>
                  </td>
                  <td>
                    ${
                      i.esLider
                        ? '<span class="badge badge--leader">★ Líder</span>'
                        : '<span class="badge badge--member">Integrante</span>'
                    }
                  </td>
                  <td style="text-align: right;">
                    <div class="card-actions" style="justify-content: flex-end;">
                      <button type="button" class="btn btn--secondary btn--sm" data-edit-integrante="${i.id}">Editar</button>
                      <button type="button" class="btn btn--danger btn--sm" data-delete-integrante="${i.id}">Eliminar</button>
                    </div>
                  </td>
                </tr>`
                )
                .join('')}
            </tbody>
          </table>
        </div>
      </div>`
    : renderEmptyState(
        'No se encontraron integrantes',
        integrantes.length
          ? 'Ajusta los filtros de búsqueda para ver otros resultados.'
          : 'Aún no hay integrantes registrados.',
        `<button type="button" class="btn btn--primary" id="emptyCreateIntegranteBtn">+ Agregar integrante</button>`
      );

  container.innerHTML = `
    <section>
      <div class="page-header">
        <div>
          <h1 class="page-title">Integrantes de Semilleros</h1>
          <p class="page-subtitle">Consulta el directorio completo, inspecciona la información de su semillero o actualiza sus datos.</p>
        </div>
        <div class="page-actions">
          <button type="button" class="btn btn--primary" id="newIntegranteBtn">+ Nuevo Integrante</button>
        </div>
      </div>

      <div class="toolbar">
        <div class="search-box">
          <span class="search-box__icon">🔍</span>
          <input type="search" class="search-box__input" id="intSearchInput"
            placeholder="Buscar por nombre, código, programa o semillero..."
            value="${escapeHtml(filters.query)}" />
        </div>
        <select class="filter-select" id="intSemilleroFilter" aria-label="Filtrar por semillero">
          <option value="">Todos los semilleros</option>
          ${semilleroOptions}
        </select>
        <label class="checkbox-row" style="padding: 8px 12px;">
          <input type="checkbox" id="intLiderFilter" ${filters.soloLideres ? 'checked' : ''} />
          <span class="text-sm">Solo líderes</span>
        </label>
      </div>

      ${tableHtml}
    </section>
  `;

  const openNew = () => openIntegranteFormModal({}, () => renderIntegrantesView(container));
  container.querySelector('#newIntegranteBtn')?.addEventListener('click', openNew);
  container.querySelector('#emptyCreateIntegranteBtn')?.addEventListener('click', openNew);

  const searchInput = container.querySelector('#intSearchInput');
  searchInput?.addEventListener('input', (e) => {
    filters.query = e.target.value;
    renderIntegrantesView(container);
    const nextInput = container.querySelector('#intSearchInput');
    if (nextInput) {
      nextInput.focus();
      nextInput.setSelectionRange(nextInput.value.length, nextInput.value.length);
    }
  });

  container.querySelector('#intSemilleroFilter')?.addEventListener('change', (e) => {
    filters.semilleroId = e.target.value;
    renderIntegrantesView(container);
  });

  container.querySelector('#intLiderFilter')?.addEventListener('change', (e) => {
    filters.soloLideres = e.target.checked;
    renderIntegrantesView(container);
  });

  // Ver información del semillero desde el integrante
  container.querySelectorAll('[data-view-semillero]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const semillero = getSemilleroById(btn.dataset.viewSemillero);
      const integrante = store.integrantes.find(
        (item) => Number(item.id) === Number(btn.dataset.integranteId)
      );
      openSemilleroPreviewModal(semillero, integrante);
    });
  });

  // Editar integrante
  container.querySelectorAll('[data-edit-integrante]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const integrante = store.integrantes.find(
        (item) => Number(item.id) === Number(btn.dataset.editIntegrante)
      );
      if (integrante) {
        openIntegranteFormModal({ integrante }, () => renderIntegrantesView(container));
      }
    });
  });

  // Eliminar integrante
  container.querySelectorAll('[data-delete-integrante]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const integrante = store.integrantes.find(
        (item) => Number(item.id) === Number(btn.dataset.deleteIntegrante)
      );
      if (!integrante) return;

      confirmDialog({
        title: 'Eliminar integrante',
        message: `¿Seguro que deseas eliminar a "${integrante.nombre}" (${integrante.codigo})?`,
        onConfirm: async () => {
          try {
            await integrantesApi.remove(integrante.id);
            await loadAllData();
            showToast('Integrante eliminado correctamente', 'success');
            renderIntegrantesView(container);
          } catch (err) {
            showToast(err.message, 'error');
            throw err;
          }
        },
      });
    });
  });
}

