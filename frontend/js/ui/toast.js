const toastRoot = () => document.getElementById('toastRoot');

export function showToast(message, type = 'info', duration = 3800) {
  const root = toastRoot();
  if (!root) return;

  const toast = document.createElement('div');
  toast.className = `toast toast--${type}`;
  toast.setAttribute('role', 'status');

  const text = document.createElement('span');
  text.textContent = message;

  const closeBtn = document.createElement('button');
  closeBtn.className = 'icon-btn icon-btn--light';
  closeBtn.type = 'button';
  closeBtn.textContent = '✕';
  closeBtn.setAttribute('aria-label', 'Cerrar notificación');
  closeBtn.addEventListener('click', () => toast.remove());

  toast.append(text, closeBtn);
  root.appendChild(toast);

  window.setTimeout(() => {
    if (toast.isConnected) toast.remove();
  }, duration);
}

