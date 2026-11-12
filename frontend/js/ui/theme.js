const THEME_KEY = 'ufps-theme';
const LIGHT_THEME_COLOR = '#f6f7f9';
const DARK_THEME_COLOR = '#141011';
const TRANSITION_DURATION = 300;

let transitionTimer;

function getStoredTheme() {
  try {
    return localStorage.getItem(THEME_KEY) === 'dark' ? 'dark' : 'light';
  } catch {
    return 'light';
  }
}

function persistTheme(theme) {
  try {
    localStorage.setItem(THEME_KEY, theme);
  } catch {
    /* El tema sigue funcionando durante la sesión aunque no se pueda persistir. */
  }
}

export function applyTheme(theme, { persist = false, animate = false } = {}) {
  const root = document.documentElement;
  const isDark = theme === 'dark';

  if (animate) {
    window.clearTimeout(transitionTimer);
    root.classList.add('theme-transition');
    transitionTimer = window.setTimeout(() => {
      root.classList.remove('theme-transition');
    }, TRANSITION_DURATION);
  }

  root.dataset.theme = isDark ? 'dark' : 'light';

  const themeColor = document.querySelector('meta[name="theme-color"]');
  themeColor?.setAttribute('content', isDark ? DARK_THEME_COLOR : LIGHT_THEME_COLOR);

  if (persist) {
    persistTheme(root.dataset.theme);
  }
}

export function setupThemeToggle() {
  const root = document.documentElement;
  const toggle = document.getElementById('themeToggle');

  applyTheme(getStoredTheme());

  if (!toggle) return;

  const syncToggle = () => {
    const isDark = root.dataset.theme === 'dark';
    toggle.setAttribute('aria-checked', String(isDark));
    toggle.setAttribute(
      'aria-label',
      isDark ? 'Desactivar modo oscuro' : 'Activar modo oscuro'
    );
    toggle.title = isDark ? 'Cambiar a modo claro' : 'Cambiar a modo oscuro';
  };

  syncToggle();

  toggle.addEventListener('click', () => {
    const nextTheme = root.dataset.theme === 'dark' ? 'light' : 'dark';
    applyTheme(nextTheme, { persist: true, animate: true });
    syncToggle();
  });
}
