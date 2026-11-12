import { store, loadAllData } from '../state/store.js';
import { lineasApi } from '../api/lineasApi.js';
import { openModal, closeModal, confirmDialog } from '../ui/modal.js';
import { showToast } from '../ui/toast.js';
import { escapeHtml, renderEmptyState } from '../ui/components.js';

let searchQuery = '';

function openLineaModal(linea = null, onSaved) {
  const isEdit = Boolean(linea);
  openModal({
    title: isEdit ? 'Editar Línea de Investigación' : 'Nueva Línea de Investigación',
    size: 'sm',
    bodyHtml: `
      <form id="lineaForm" class="form-grid">
        <div class="form-field form-field--full">
          <label class="form-label" for="linNombre">Nombre de la línea *</label>
          <input class="form-control" id="linNombre" name="nombre" required maxlength="180"
            value="${escapeHtml(linea?.nombre || '')}" placeholder="Ej. Inteligencia Artificial" />
        </div>
        <div class="form-field form-field--full" id="linFormError"></div>
      </form>
    `,
    footerHtml: `
      <button type="button" class="btn btn--secondary" data-modal-close>Cancelar</button>
      <button type="submit" form="lineaForm" class="btn btn--primary" id="saveLinBtn">
        ${isEdit ? 'Guardar cambios' : 'Crear línea'}
      </button>
    `,
    onMount: (backdrop) => {
      const form = backdrop.querySelector('#lineaForm');
      const errorBox = backdrop.querySelector('#linFormError');
      const saveBtn = backdrop.querySelector('#saveLinBtn');

      form.addEventListener('submit', async (e) => {
        e.preventDefault();
        errorBox.innerHTML = '';
        const fd = new FormData(form);
        const payload = {
          nombre: String(fd.get('nombre') || '').trim(),
        };

        saveBtn.disabled = true;
        try {
          if (isEdit) {
            await lineasApi.update(linea.id, payload);
            showToast('Línea actualizada', 'success');
          } else {
            await lineasApi.create(payload);
            showToast('Línea creada', 'success');
          }
          await loadAllData();
          closeModal();
          onSaved();
        } catch (err) {
          errorBox.innerHTML = `<div class="form-error">${escapeHtml(err.message)}</div>`;
          saveBtn.disabled = false;
        }
      });
    },
  });
}

export function renderLineasView(container) {
  const { lineas, semilleros } = store;
  const q = searchQuery.trim().toLowerCase();

  const filtered = lineas.filter(
    (l) => !q || l.nombre.toLowerCase().includes(q)
  );

  const tableHtml = filtered.length
    ? `
      <div class="table-card">
        <div class="table-responsive">
          <table class="data-table">
            <thead>
              <tr>
                <th>Línea de Investigación</th>
                <th>Semilleros Asociados</th>
                <th style="text-align: right;">Acciones</th>
              </tr>
            </thead>
            <tbody>
              ${filtered
                .map((l) => {
                  const associated = semilleros.filter((s) =>
                    (s.lineas || []).some((sl) => Number(sl.id) === Number(l.id))
                  );
                  const semBadges = associated.length
                    ? associated
                        .map(
                          (s) =>
                            `<a href="#/semilleros/${s.id}" class="semillero-link-btn" style="margin-right: 4px;">${escapeHtml(s.sigla)}</a>`
                        )
                        .join('')
                    : '<span class="text-sm text-muted">Sin semilleros asociados</span>';

                  return `
                  <tr>
                    <td>
                      <span class="tag">${escapeHtml(l.nombre)}</span>
                    </td>
                    <td>${semBadges}</td>
                    <td style="text-align: right;">
                      <div class="card-actions" style="justify-content: flex-end;">
                        <button type="button" class="btn btn--secondary btn--sm" data-edit-linea="${l.id}">Editar</button>
                        <button type="button" class="btn btn--danger btn--sm" data-delete-linea="${l.id}">Eliminar</button>
                      </div>
                    </td>
                  </tr>`;
                })
                .join('')}
            </tbody>
          </table>
        </div>
      </div>`
    : renderEmptyState(
        'No se encontraron líneas de investigación',
        lineas.length
          ? 'Ajusta el término de búsqueda.'
          : 'Agrega líneas temáticas para asociarlas a los semilleros.',
        `<button type="button" class="btn btn--primary" id="emptyNewLineaBtn">+ Nueva Línea</button>`
      );

  container.innerHTML = `
    <section>
      <div class="page-header">
        <div>
          <h1 class="page-title">Líneas de Investigación</h1>
          <p class="page-subtitle">Administra las áreas temáticas y consulta qué semilleros trabajan en cada línea.</p>
        </div>
        <div class="page-actions">
          <button type="button" class="btn btn--primary" id="newLineaBtn">+ Nueva Línea</button>
        </div>
      </div>

      <div class="toolbar">
        <div class="search-box">
          <span class="search-box__icon">🔍</span>
          <input type="search" class="search-box__input" id="linSearchInput"
            placeholder="Buscar línea de investigación..."
            value="${escapeHtml(searchQuery)}" />
        </div>
      </div>

      ${tableHtml}
    </section>
  `;

  const openNew = () => openLineaModal(null, () => renderLineasView(container));
  container.querySelector('#newLineaBtn')?.addEventListener('click', openNew);
  container.querySelector('#emptyNewLineaBtn')?.addEventListener('click', openNew);

  const searchInput = container.querySelector('#linSearchInput');
  searchInput?.addEventListener('input', (e) => {
    searchQuery = e.target.value;
    renderLineasView(container);
    const nextInput = container.querySelector('#linSearchInput');
    if (nextInput) {
      nextInput.focus();
      nextInput.setSelectionRange(nextInput.value.length, nextInput.value.length);
    }
  });

  container.querySelectorAll('[data-edit-linea]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const l = store.lineas.find((item) => Number(item.id) === Number(btn.dataset.editLinea));
      if (l) openLineaModal(l, () => renderLineasView(container));
    });
  });

  container.querySelectorAll('[data-delete-linea]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const l = store.lineas.find((item) => Number(item.id) === Number(btn.dataset.deleteLinea));
      if (!l) return;
      confirmDialog({
        title: 'Eliminar línea',
        message: `¿Deseas eliminar "${l.nombre}"? Solo es posible si no está asociada a ningún semillero.`,
        onConfirm: async () => {
          try {
            await lineasApi.remove(l.id);
            await loadAllData();
            showToast('Línea eliminada', 'success');
            renderLineasView(container);
          } catch (err) {
            showToast(err.message, 'error');
            throw err;
          }
        },
      });
    });
  });
}

