/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        metro: {
          dark: '#0B0B0C',
          darker: '#060607',
          cardDark: '#13110E',
          cardDarkHover: '#1A1713',
          borderDark: 'rgba(201, 162, 74, 0.15)',
          paper: '#FFFDF8',
          soft: '#F6F1E7',
          ink: '#16130E',
          sub: '#6E6658',
          faint: '#A39A8B',
          line: 'rgba(22, 19, 14, 0.08)',
          gold: '#C9A24A',
          goldDeep: '#8A6A1F',
          goldLight: '#FBF5E6',
          goldGlow: 'rgba(201, 162, 74, 0.25)',
          ok: '#2F9A63',
          okLight: '#E8F6EE',
          wait: '#E0A42E',
          waitLight: '#FEF6E6',
          move: '#4E7BD8',
          moveLight: '#EEF4FD',
          bad: '#D2553F',
          badLight: '#FDF0ED',
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', '-apple-system', 'Segoe UI', 'sans-serif'],
        display: ['Cinzel', 'Playfair Display', 'serif', 'system-ui'],
      },
      boxShadow: {
        'royal': '0 14px 34px -18px rgba(60, 44, 12, 0.28)',
        'royal-hover': '0 22px 44px -18px rgba(60, 44, 12, 0.36)',
        'gold-glow': '0 0 25px rgba(201, 162, 74, 0.35)',
        'dark-card': '0 10px 30px -10px rgba(0, 0, 0, 0.5), inset 0 1px 0 rgba(201, 162, 74, 0.15)',
      }
    },
  },
  plugins: [],
}
