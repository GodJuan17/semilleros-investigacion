import { store, loadAllData } from '../state/store.js';
import { directoresApi } from '../api/directoresApi.js';
import { openModal, closeModal, confirmDialog } from '../ui/modal.js';
import { showToast } from '../ui/toast.js';
import { escapeHtml, getInitials, renderEmptyState } from '../ui/components.js';

let searchQuery = '';

function openDirectorModal(director = null, onSaved) {
  const isEdit = Boolean(director);
  openModal({
    title: isEdit ? 'Editar Director' : 'Nuevo Director',
    size: 'sm',
    bodyHtml: `
      <form id="directorForm" class="form-grid">
        <div class="form-field form-field--full">
          <label class="form-label" for="dirNombre">Nombre completo *</label>
          <input class="form-control" id="dirNombre" name="nombre" required maxlength="150"
            value="${escapeHtml(director?.nombre || '')}" placeholder="Ej. Dra. Ana María Pérez" />
        </div>
        <div class="form-field form-field--full">
          <label class="form-label" for="dirCorreo">Correo institucional *</label>
          <input class="form-control" id="dirCorreo" name="correo" type="email" required maxlength="150"
            value="${escapeHtml(director?.correo || '')}" placeholder="nombre@ufps.edu.co" />
        </div>
        <div class="form-field form-field--full" id="dirFormError"></div>
      </form>
    `,
    footerHtml: `
      <button type="button" class="btn btn--secondary" data-modal-close>Cancelar</button>
      <button type="submit" form="directorForm" class="btn btn--primary" id="saveDirBtn">
        ${isEdit ? 'Guardar cambios' : 'Crear director'}
      </button>
    `,
    onMount: (backdrop) => {
      const form = backdrop.querySelector('#directorForm');
      const errorBox = backdrop.querySelector('#dirFormError');
      const saveBtn = backdrop.querySelector('#saveDirBtn');

      form.addEventListener('submit', async (e) => {
        e.preventDefault();
        errorBox.innerHTML = '';
        const fd = new FormData(form);
        const payload = {
          nombre: String(fd.get('nombre') || '').trim(),
          correo: String(fd.get('correo') || '').trim(),
        };

        saveBtn.disabled = true;
        try {
          if (isEdit) {
            await directoresApi.update(director.id, payload);
            showToast('Director actualizado', 'success');
          } else {
            await directoresApi.create(payload);
            showToast('Director creado', 'success');
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

export function renderDirectoresView(container) {
  const { directores, semilleros } = store;
  const q = searchQuery.trim().toLowerCase();

  const filtered = directores.filter(
    (d) =>
      !q ||
      d.nombre.toLowerCase().includes(q) ||
      d.correo.toLowerCase().includes(q)
  );

  const tableHtml = filtered.length
    ? `
      <div class="table-card">
        <div class="table-responsive">
          <table class="data-table">
            <thead>
              <tr>
                <th>Director</th>
                <th>Correo Institucional</th>
                <th>Semilleros a cargo</th>
                <th style="text-align: right;">Acciones</th>
              </tr>
            </thead>
            <tbody>
              ${filtered
                .map((d) => {
                  const assigned = semilleros.filter(
                    (s) => Number(s.director?.id) === Number(d.id)
                  );
                  const semBadges = assigned.length
                    ? assigned
                        .map(
                          (s) =>
                            `<a href="#/semilleros/${s.id}" class="semillero-link-btn" style="margin-right: 4px;">${escapeHtml(s.sigla)}</a>`
                        )
                        .join('')
                    : '<span class="text-sm text-muted">Sin semilleros</span>';

                  return `
                  <tr>
                    <td>
                      <div class="person-cell">
                        <div class="avatar">${escapeHtml(getInitials(d.nombre))}</div>
                        <strong>${escapeHtml(d.nombre)}</strong>
                      </div>
                    </td>
                    <td>${escapeHtml(d.correo)}</td>
                    <td>${semBadges}</td>
                    <td style="text-align: right;">
                      <div class="card-actions" style="justify-content: flex-end;">
                        <button type="button" class="btn btn--secondary btn--sm" data-edit-director="${d.id}">Editar</button>
                        <button type="button" class="btn btn--danger btn--sm" data-delete-director="${d.id}">Eliminar</button>
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
        'No se encontraron directores',
        directores.length
          ? 'Ajusta el término de búsqueda.'
          : 'Crea un director para poder asignarlo a los semilleros.',
        `<button type="button" class="btn btn--primary" id="emptyNewDirectorBtn">+ Nuevo Director</button>`
      );

  container.innerHTML = `
    <section>
      <div class="page-header">
        <div>
          <h1 class="page-title">Directores de Semilleros</h1>
          <p class="page-subtitle">Gestiona los docentes responsables y consulta los semilleros que dirigen.</p>
        </div>
        <div class="page-actions">
          <button type="button" class="btn btn--primary" id="newDirectorBtn">+ Nuevo Director</button>
        </div>
      </div>

      <div class="toolbar">
        <div class="search-box">
          <span class="search-box__icon">🔍</span>
          <input type="search" class="search-box__input" id="dirSearchInput"
            placeholder="Buscar director por nombre o correo..."
            value="${escapeHtml(searchQuery)}" />
        </div>
      </div>

      ${tableHtml}
    </section>
  `;

  const openNew = () => openDirectorModal(null, () => renderDirectoresView(container));
  container.querySelector('#newDirectorBtn')?.addEventListener('click', openNew);
  container.querySelector('#emptyNewDirectorBtn')?.addEventListener('click', openNew);

  const searchInput = container.querySelector('#dirSearchInput');
  searchInput?.addEventListener('input', (e) => {
    searchQuery = e.target.value;
    renderDirectoresView(container);
    const nextInput = container.querySelector('#dirSearchInput');
    if (nextInput) {
      nextInput.focus();
      nextInput.setSelectionRange(nextInput.value.length, nextInput.value.length);
    }
  });

  container.querySelectorAll('[data-edit-director]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const d = store.directores.find((item) => Number(item.id) === Number(btn.dataset.editDirector));
      if (d) openDirectorModal(d, () => renderDirectoresView(container));
    });
  });

  container.querySelectorAll('[data-delete-director]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const d = store.directores.find((item) => Number(item.id) === Number(btn.dataset.deleteDirector));
      if (!d) return;
      confirmDialog({
        title: 'Eliminar director',
        message: `¿Deseas eliminar a "${d.nombre}"? Solo es posible si no tiene semilleros asociados.`,
        onConfirm: async () => {
          try {
            await directoresApi.remove(d.id);
            await loadAllData();
            showToast('Director eliminado', 'success');
            renderDirectoresView(container);
          } catch (err) {
            showToast(err.message, 'error');
            throw err;
          }
        },
      });
    });
  });
}

