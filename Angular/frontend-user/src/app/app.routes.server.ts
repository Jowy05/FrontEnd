import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  // rutas públicas, el servidor las puede renderizar
  { path: 'login', renderMode: RenderMode.Prerender },
  { path: 'register', renderMode: RenderMode.Prerender },
  // rutas privadas, solo el navegador las renderiza (necesitan localStorage)
  { path: 'perfil', renderMode: RenderMode.Client },
  { path: 'reservas', renderMode: RenderMode.Client },
  { path: 'historial', renderMode: RenderMode.Client },
  { path: 'resultado/:id', renderMode: RenderMode.Client },
  { path: '**', renderMode: RenderMode.Prerender }
];
