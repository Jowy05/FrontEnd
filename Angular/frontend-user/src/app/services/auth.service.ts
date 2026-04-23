import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { User } from '../models/user.model'; // Importamos la interfaz que creaste

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) {}

  login(credentials: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, credentials).pipe(
      tap((res: any) => {
        if (res && res.token) {
          localStorage.setItem('token', res.token);
        }
      })
    );
  }

  // Nuevo metodo: Envia los datos del nuevo usuario al backend
  register(userData: User): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, userData).pipe(
      tap((res: any) => {
        // Tu backend tambien devuelve un token al registrar, lo guardamos
        if (res && res.token) {
          localStorage.setItem('token', res.token);
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
  }
}