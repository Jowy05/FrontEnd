import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { NotificationService } from '../services/notification.service';

// añade el token JWT a todas las peticiones y gestiona errores de sesión
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const platformId = inject(PLATFORM_ID);
  const router = inject(Router);
  const notif = inject(NotificationService);

  const esRutaPublica = req.url.includes('/api/auth/');
  if (isPlatformBrowser(platformId) && req.url.startsWith('http://localhost:8080/api/') && !esRutaPublica) {
    const token = localStorage.getItem('token');
    if (token) {
      req = req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      });
    }
  }

  return next(req).pipe(
    catchError((err: HttpErrorResponse) => {
      if (err.status === 0) {
        notif.mostrar('No se puede conectar al servidor. ¿Está el backend encendido?', 'error');
      } else if (err.status === 500) {
        notif.mostrar('Error en el servidor (500). Puede que la base de datos no responda.', 'warn');
      } else if (err.status === 401 || err.status === 403) {
        if (isPlatformBrowser(platformId)) {
          localStorage.removeItem('token');
          localStorage.removeItem('userId');
        }
        router.navigate(['/login']);
      }
      return throwError(() => err);
    })
  );
};