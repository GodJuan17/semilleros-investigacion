import { store, loadAllData, getSemilleroById } from '../state/store.js';
import { productosApi } from '../api/productosApi.js';
import { confirmDialog } from '../ui/modal.js';
import { showToast } from '../ui/toast.js';
import {
  escapeHtml,
  openProductoDetailModal,
  openProductoFormModal,
  openSemilleroPreviewModal,
  renderEmptyState,
} from '../ui/components.js';

let filters = {
  query: '',
  semilleroId: '',
  categoria: '',
};

let viewMode = 'cards'; // 'cards' | 'table'

export function renderProductosView(container) {
  const { productos, semilleros, tiposProducto } = store;

  const categorias = Array.from(new Set((tiposProducto || []).map((t) => t.categoria)));

  const filtered = productos.filter((p) => {
    const q = filters.query.trim().toLowerCase();
    const matchesQuery =
      !q ||
      p.nombre.toLowerCase().includes(q) ||
      (p.descripcion || '').toLowerCase().includes(q) ||
      (p.tipoNombre || '').toLowerCase().includes(q) ||
      (p.semilleroSigla || '').toLowerCase().includes(q) ||
      (p.semilleroNombre || '').toLowerCase().includes(q) ||
      (p.integrantes || []).some((i) => i.nombre.toLowerCase().includes(q));

    const matchesSemillero =
      !filters.semilleroId || Number(p.semilleroId) === Number(filters.semilleroId);

    const matchesCategoria =
      !filters.categoria || p.tipoCategoria === filters.categoria;

    return matchesQuery && matchesSemillero && matchesCategoria;
  });

  const semilleroOptions = semilleros
    .map(
      (s) =>
        `<option value="${s.id}" ${Number(filters.semilleroId) === Number(s.id) ? 'selected' : ''}>${escapeHtml(s.sigla)} — ${escapeHtml(s.nombre)}</option>`
    )
    .join('');

  const categoriaOptions = categorias
    .map(
      (c) =>
        `<option value="${escapeHtml(c)}" ${filters.categoria === c ? 'selected' : ''}>${escapeHtml(c)}</option>`
    )
    .join('');

  const getDestacado = (p) => {
    const integrantes = p.integrantes || [];
    const destacado = integrantes.find((i) => i.esLider) || integrantes[0] || null;
    const extraCount = Math.max(0, integrantes.length - 1);
    return { destacado, extraCount };
  };

  const cardsHtml = `
    <div class="cards-grid">
      ${filtered
        .map((p) => {
          const { destacado, extraCount } = getDestacado(p);

          return `
          <article class="semillero-card">
            <div class="semillero-card__header">
              <button type="button" class="sigla-badge" style="border: 0; cursor: pointer;" data-view-semillero="${p.semilleroId}" title="Ver información del semillero">
                ${escapeHtml(p.semilleroSigla)}
              </button>
              <span class="badge badge--member">${escapeHtml(p.tipoCategoria)}</span>
            </div>

            <div>
              <h2 class="semillero-card__title" data-view-producto="${p.id}">${escapeHtml(p.nombre)}</h2>
              <p class="semillero-card__group">${escapeHtml(p.tipoNombre)}</p>
            </div>

            <hr class="card-divider" />

            <div class="semillero-card__meta">
              <div class="meta-row" style="justify-content: space-between; align-items: center;">
                <span class="meta-row__label">${destacado?.esLider ? 'Líder participante:' : 'Participante:'}</span>
                ${
                  destacado
                    ? `<span>
                        <strong>${escapeHtml(destacado.nombre)}</strong>${destacado.esLider ? ' ★' : ''}
                        ${extraCount > 0 ? `<span class="text-muted"> (+${extraCount})</span>` : ''}
                      </span>`
                    : '<span class="text-muted">Sin participantes</span>'
                }
              </div>
            </div>

            <div class="semillero-card__footer">
              <button type="button" class="btn btn--soft-red btn--sm" data-view-producto="${p.id}">Ver más</button>
              <div class="card-actions">
                <button type="button" class="btn btn--secondary btn--sm" data-edit-producto="${p.id}">Editar</button>
                <button type="button" class="btn btn--danger btn--sm" data-delete-producto="${p.id}">Eliminar</button>
              </div>
            </div>
          </article>`;
        })
        .join('')}
    </div>`;

  const tableHtml = `
    <div class="table-card">
      <div class="table-responsive">
        <table class="data-table">
          <thead>
            <tr>
              <th>Producto</th>
              <th>Semillero</th>
              <th>Tipología Minciencias</th>
              <th>Líder / Participante</th>
              <th style="text-align: right;">Acciones</th>
            </tr>
          </thead>
          <tbody>
            ${filtered
              .map((p) => {
                const { destacado, extraCount } = getDestacado(p);
                return `
                <tr>
                  <td>
                    <strong style="cursor: pointer;" data-view-producto="${p.id}">${escapeHtml(p.nombre)}</strong>
                  </td>
                  <td>
                    <button type="button" class="semillero-link-btn" data-view-semillero="${p.semilleroId}" title="Ver información del semillero">
                      <span>${escapeHtml(p.semilleroSigla)}</span>
                    </button>
                  </td>
                  <td>
                    <div class="text-sm" style="font-weight: 600;">${escapeHtml(p.tipoNombre)}</div>
                    <span class="badge badge--member" style="margin-top: 4px;">${escapeHtml(p.tipoCategoria)}</span>
                  </td>
                  <td>
                    ${
                      destacado
                        ? `<span>
                            <strong>${escapeHtml(destacado.nombre)}</strong>${destacado.esLider ? ' ★' : ''}
                            ${extraCount > 0 ? `<span class="text-muted"> (+${extraCount})</span>` : ''}
                          </span>`
                        : '<span class="text-muted">Sin participantes</span>'
                    }
                  </td>
                  <td style="text-align: right;">
                    <div class="card-actions" style="justify-content: flex-end;">
                      <button type="button" class="btn btn--soft-red btn--sm" data-view-producto="${p.id}">Ver más</button>
                      <button type="button" class="btn btn--secondary btn--sm" data-edit-producto="${p.id}">Editar</button>
                      <button type="button" class="btn btn--danger btn--sm" data-delete-producto="${p.id}">Eliminar</button>
                    </div>
                  </td>
                </tr>`;
              })
              .join('')}
          </tbody>
        </table>
      </div>
    </div>`;

  const contentHtml = filtered.length
    ? viewMode === 'table'
      ? tableHtml
      : cardsHtml
    : renderEmptyState(
        'No se encontraron productos',
        productos.length
          ? 'Ajusta los filtros de búsqueda para ver otros resultados.'
          : 'Aún no hay productos registrados en los semilleros.',
        `<button type="button" class="btn btn--primary" id="emptyCreateProductoBtn">+ Registrar producto</button>`
      );

  container.innerHTML = `
    <section>
      <div class="page-header">
        <div>
          <h1 class="page-title">Productos de Investigación</h1>
          <p class="page-subtitle">Gestiona los productos desarrollados por los semilleros según la tipología oficial de Minciencias.</p>
        </div>
        <div class="page-actions">
          <button type="button" class="btn btn--primary" id="newProductoBtn">+ Nuevo Producto</button>
        </div>
      </div>

      <div class="toolbar">
        <div class="search-box">
          <span class="search-box__icon">🔍</span>
          <input type="search" class="search-box__input" id="prodSearchInput"
            placeholder="Buscar por nombre, tipo, integrante o semillero..."
            value="${escapeHtml(filters.query)}" />
        </div>
        <select class="filter-select" id="prodSemilleroFilter" aria-label="Filtrar por semillero">
          <option value="">Todos los semilleros</option>
          ${semilleroOptions}
        </select>
        <select class="filter-select" id="prodCategoriaFilter" aria-label="Filtrar por categoría Minciencias">
          <option value="">Todas las categorías Minciencias</option>
          ${categoriaOptions}
        </select>
        <div class="view-toggle" role="group" aria-label="Modo de visualización">
          <button type="button" class="view-toggle__btn ${viewMode === 'cards' ? 'is-active' : ''}" data-view-mode="cards">Tarjetas</button>
          <button type="button" class="view-toggle__btn ${viewMode === 'table' ? 'is-active' : ''}" data-view-mode="table">Tabla</button>
        </div>
      </div>

      ${contentHtml}
    </section>
  `;

  const openNew = () => openProductoFormModal({}, () => renderProductosView(container));
  container.querySelector('#newProductoBtn')?.addEventListener('click', openNew);
  container.querySelector('#emptyCreateProductoBtn')?.addEventListener('click', openNew);

  const searchInput = container.querySelector('#prodSearchInput');
  searchInput?.addEventListener('input', (e) => {
    filters.query = e.target.value;
    renderProductosView(container);
    const nextInput = container.querySelector('#prodSearchInput');
    if (nextInput) {
      nextInput.focus();
      nextInput.setSelectionRange(nextInput.value.length, nextInput.value.length);
    }
  });

  container.querySelector('#prodSemilleroFilter')?.addEventListener('change', (e) => {
    filters.semilleroId = e.target.value;
    renderProductosView(container);
  });

  container.querySelector('#prodCategoriaFilter')?.addEventListener('change', (e) => {
    filters.categoria = e.target.value;
    renderProductosView(container);
  });

  container.querySelectorAll('[data-view-mode]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const nextMode = btn.dataset.viewMode;
      if (nextMode && nextMode !== viewMode) {
        viewMode = nextMode;
        renderProductosView(container);
      }
    });
  });

  container.querySelectorAll('[data-view-semillero]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const semillero = getSemilleroById(btn.dataset.viewSemillero);
      openSemilleroPreviewModal(semillero);
    });
  });

  container.querySelectorAll('[data-view-producto]').forEach((el) => {
    el.addEventListener('click', () => {
      const producto = store.productos.find(
        (item) => Number(item.id) === Number(el.dataset.viewProducto)
      );
      if (producto) {
        openProductoDetailModal(producto, (prodToEdit) => {
          openProductoFormModal({ producto: prodToEdit }, () => renderProductosView(container));
        });
      }
    });
  });

  container.querySelectorAll('[data-edit-producto]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const producto = store.productos.find(
        (item) => Number(item.id) === Number(btn.dataset.editProducto)
      );
      if (producto) {
        openProductoFormModal({ producto }, () => renderProductosView(container));
      }
    });
  });

  container.querySelectorAll('[data-delete-producto]').forEach((btn) => {
    btn.addEventListener('click', () => {
      const producto = store.productos.find(
        (item) => Number(item.id) === Number(btn.dataset.deleteProducto)
      );
      if (!producto) return;

      confirmDialog({
        title: 'Eliminar producto',
        message: `¿Seguro que deseas eliminar el producto "${producto.nombre}"?`,
        onConfirm: async () => {
          try {
            await productosApi.remove(producto.id);
            await loadAllData();
            showToast('Producto eliminado correctamente', 'success');
            renderProductosView(container);
          } catch (err) {
            showToast(err.message, 'error');
            throw err;
          }
        },
      });
    });
  });
}
