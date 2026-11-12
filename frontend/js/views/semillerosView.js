import { store, loadAllData, getIntegrantesBySemillero } from '../state/store.js';
import { semillerosApi } from '../api/semillerosApi.js';
import { confirmDialog } from '../ui/modal.js';
import { showToast } from '../ui/toast.js';
import {
  escapeHtml,
  openSemilleroFormModal,
  renderEmptyState,
} from '../ui/components.js';

let filters = {
  query: '',
  lineaId: '',
  directorId: '',
};

export function renderSemillerosView(container) {
  const { semilleros, integrantes, lineas, directores } = store;

  const filtered = semilleros.filter((s) => {
    const q = filters.query.trim().toLowerCase();
    const matchesQuery =
      !q ||
      s.nombre.toLowerCase().includes(q) ||
      s.sigla.toLowerCase().includes(q) ||
      s.grupoInvestigacion.toLowerCase().includes(q) ||
      (s.director?.nombre || '').toLowerCase().includes(q);

    const matchesLinea =
      !filters.lineaId ||
      (s.lineas || []).some((l) => Number(l.id) === Number(filters.lineaId));

    const matchesDirector =
      !filters.directorId ||
      Number(s.director?.id) === Number(filters.directorId);

    return matchesQuery && matchesLinea && matchesDirector;
  });

  const lineaOptions = lineas
    .map(
      (l) =>
        `<option value="${l.id}" ${Number(filters.lineaId) === Number(l.id) ? 'selected' : ''}>${escapeHtml(l.nombre)}</option>`
    )
    .join('');

  const directorOptions = directores
    .map(
      (d) =>
        `<option value="${d.id}" ${Number(filters.directorId) === Number(d.id) ? 'selected' : ''}>${escapeHtml(d.nombre)}</option>`
    )
    .join('');

  const cardsHtml = filtered.length
    ? `<div class="cards-grid">
        ${filtered
          .map((s) => {
            const count = getIntegrantesBySemillero(s.id).length;
            const lineasHtml = (s.lineas || []).length
              ? s.lineas.map((l) => `<span class="tag">${escapeHtml(l.nombre)}</span>`).join('')
              : '<span class="text-sm text-muted">Sin líneas asociadas</span>';

            return `
              <article class="semillero-card" data-semillero-id="${s.id}">
                <div class="semillero-card__header">
                  <div>
                    <span class="sigla-badge">${escapeHtml(s.sigla)}</span>
                  </div>
                  <div class="card-actions">
                    <button type="button" class="btn btn--secondary btn--sm" data-action="edit" data-id="${s.id}">
                      Editar
                    </button>
                    <button type="button" class="btn btn--danger btn--sm" data-action="delete" data-id="${s.id}">
                      Eliminar
                    </button>
                  </div>
                </div>

                <div>
                  <h2 class="semillero-card__title" data-action="open" data-id="${s.id}">
                    ${escapeHtml(s.nombre)}
                  </h2>
                  <p class="semillero-card__group">${escapeHtml(s.grupoInvestigacion)}</p>
                </div>

                <div class="semillero-card__meta">
                  <div class="meta-row">
                    <span class="meta-row__label">Director</span>
                    <span class="meta-row__value">${escapeHtml(s.director?.nombre || 'No asignado')}</span>
                  </div>
                  <div class="meta-row">
                    <span class="meta-row__label">Integrantes</span>
                    <span class="meta-row__value">${count} registrado${count === 1 ? '' : 's'}</span>
                  </div>
                </div>

                <div class="tags-wrap">${lineasHtml}</div>

                <div class="semillero-card__footer">
                  <span class="text-sm text-muted">${(s.lineas || []).length} línea(s)</span>
                  <a href="#/semilleros/${s.id}" class="btn btn--soft-red btn--sm">
                    Ver líneas e integrantes →
                  </a>
                </div>
              </article>
            `;
          })
          .join('')}
      </div>`
    : renderEmptyState(
        'No se encontraron semilleros',
        semilleros.length
          ? 'Prueba ajustando los filtros o el término de búsqueda.'
          : 'Aún no hay semilleros registrados en la base de datos.',
        `<button type="button" class="btn btn--primary" id="emptyCreateSemilleroBtn">+ Crear primer semillero</button>`
      );

  container.innerHTML = `
    <section>
      <div class="page-header">
        <div>
          <h1 class="page-title">Semilleros de Investigación</h1>
          <p class="page-subtitle">Explora, administra y consulta líneas e integrantes de los semilleros UFPS Cúcuta.</p>
        </div>
        <div class="page-actions">
          <button type="button" class="btn btn--primary" id="newSemilleroBtn">+ Nuevo Semillero</button>
        </div>
      </div>

      <div class="kpi-grid">
        <div class="kpi-card">
          <span class="kpi-label">Semilleros Activos</span>
          <span class="kpi-value">${semilleros.length}</span>
          <span class="kpi-hint">Registrados en el sistema</span>
        </div>
        <div class="kpi-card">
          <span class="kpi-label">Integrantes</span>
          <span class="kpi-value">${integrantes.length}</span>
          <span class="kpi-hint">Estudiantes vinculados</span>
        </div>
        <div class="kpi-card">
          <span class="kpi-label">Líneas de Investigación</span>
          <span class="kpi-value">${lineas.length}</span>
          <span class="kpi-hint">Áreas temáticas disponibles</span>
        </div>
        <div class="kpi-card">
          <span class="kpi-label">Directores</span>
          <span class="kpi-value">${directores.length}</span>
          <span class="kpi-hint">Docentes responsables</span>
        </div>
      </div>

      <div class="toolbar">
        <div class="search-box">
          <span class="search-box__icon">🔍</span>
          <input type="search" class="search-box__input" id="semSearchInput"
            placeholder="Buscar por nombre, sigla, grupo o director..."
            value="${escapeHtml(filters.query)}" />
        </div>
        <select class="filter-select" id="semLineaFilter" aria-label="Filtrar por línea">
          <option value="">Todas las líneas</option>
          ${lineaOptions}
        </select>
        <select class="filter-select" id="semDirectorFilter" aria-label="Filtrar por director">
          <option value="">Todos los directores</option>
          ${directorOptions}
        </select>
      </div>

      ${cardsHtml}
    </section>
  `;

  // Eventos de creación y filtros
  container.querySelector('#newSemilleroBtn')?.addEventListener('click', () => {
    openSemilleroFormModal(null, () => renderSemillerosView(container));
  });

  container.querySelector('#emptyCreateSemilleroBtn')?.addEventListener('click', () => {
    openSemilleroFormModal(null, () => renderSemillerosView(container));
  });

  const searchInput = container.querySelector('#semSearchInput');
  searchInput?.addEventListener('input', (e) => {
    filters.query = e.target.value;
    renderSemillerosView(container);
    const nextInput = container.querySelector('#semSearchInput');
    if (nextInput) {
      nextInput.focus();
      nextInput.setSelectionRange(nextInput.value.length, nextInput.value.length);
    }
  });

  container.querySelector('#semLineaFilter')?.addEventListener('change', (e) => {
    filters.lineaId = e.target.value;
    renderSemillerosView(container);
  });

  container.querySelector('#semDirectorFilter')?.addEventListener('change', (e) => {
    filters.directorId = e.target.value;
    renderSemillerosView(container);
  });

  // Acciones en tarjetas
  container.querySelectorAll('[data-action="open"]').forEach((el) => {
    el.addEventListener('click', () => {
      window.location.hash = `#/semilleros/${el.dataset.id}`;
    });
  });

  container.querySelectorAll('[data-action="edit"]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const sem = store.semilleros.find((item) => Number(item.id) === Number(btn.dataset.id));
      if (sem) {
        openSemilleroFormModal(sem, () => renderSemillerosView(container));
      }
    });
  });

  container.querySelectorAll('[data-action="delete"]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const sem = store.semilleros.find((item) => Number(item.id) === Number(btn.dataset.id));
      if (!sem) return;

      confirmDialog({
        title: `Eliminar semillero ${sem.sigla}`,
        message: `¿Seguro que deseas eliminar "${sem.nombre}"? Esta acción eliminará también sus asociaciones y no se puede deshacer.`,
        onConfirm: async () => {
          try {
            await semillerosApi.remove(sem.id);
            await loadAllData();
            showToast('Semillero eliminado', 'success');
            renderSemillerosView(container);
          } catch (err) {
            showToast(err.message, 'error');
            throw err;
          }
        },
      });
    });
  });
}

