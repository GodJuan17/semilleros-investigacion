import { escapeHtml } from './components.js';

const modalRoot = () => document.getElementById('modalRoot');

export function closeModal() {
  const root = modalRoot();
  if (root) root.innerHTML = '';
}

export function openModal({ title, bodyHtml, footerHtml = '', size = 'md', onMount }) {
  const root = modalRoot();
  if (!root) return;

  const sizeClass = size === 'sm' ? 'modal--sm' : '';

  root.innerHTML = `
    <div class="modal-backdrop" id="activeModalBackdrop" role="dialog" aria-modal="true" aria-labelledby="modalTitle">
      <div class="modal ${sizeClass}">
        <div class="modal__header">
          <h3 class="modal__title" id="modalTitle">${escapeHtml(title)}</h3>
          <button class="icon-btn" type="button" data-modal-close aria-label="Cerrar">✕</button>
        </div>
        <div class="modal__body">${bodyHtml}</div>
        ${footerHtml ? `<div class="modal__footer">${footerHtml}</div>` : ''}
      </div>
    </div>
  `;

  const backdrop = document.getElementById('activeModalBackdrop');
  backdrop.addEventListener('click', (e) => {
    if (e.target === backdrop || e.target.closest('[data-modal-close]')) {
      closeModal();
    }
  });

  const escHandler = (e) => {
    if (e.key === 'Escape') {
      closeModal();
      document.removeEventListener('keydown', escHandler);
    }
  };
  document.addEventListener('keydown', escHandler);

  if (typeof onMount === 'function') {
    onMount(backdrop);
  }
}

export function confirmDialog({
  title,
  message,
  confirmLabel = 'Eliminar',
  confirmVariant = 'danger',
  onConfirm,
}) {
  openModal({
    title,
    size: 'sm',
    bodyHtml: `<p class="text-muted">${escapeHtml(message)}</p>`,
    footerHtml: `
      <button type="button" class="btn btn--secondary" data-modal-close>Cancelar</button>
      <button type="button" class="btn btn--${confirmVariant}" id="confirmActionBtn">${escapeHtml(confirmLabel)}</button>
    `,
    onMount: (backdrop) => {
      const btn = backdrop.querySelector('#confirmActionBtn');
      btn.addEventListener('click', async () => {
        btn.disabled = true;
        btn.textContent = 'Procesando...';
        try {
          await onConfirm();
          closeModal();
        } catch {
          btn.disabled = false;
          btn.textContent = confirmLabel;
        }
      });
    },
  });
}

