import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  // Cambia el puerto si tu API usa uno distinto al 8080
  private apiUrl = 'http://localhost:8080/api/auth'; 

  constructor(private http: HttpClient) {}

  // Enviamos los datos al servidor para validar al usuario
  login(credentials: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap((res: any) => {
        // Si el login es un exito, guardamos la 'llave' (JWT) en el navegador
        if (res && res.token) {
          localStorage.setItem('token', res.token);
        }
      })
    );
  }

  // Metodo para cerrar sesion borrando el rastro del token
  logout(): void {
    localStorage.removeItem('token');
  }
}