import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  // Prepara la cabecera con el token JWT guardado
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  }

  // Peticion GET para obtener tu perfil
  getProfile(): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/profile`, { headers: this.getAuthHeaders() });
  }

  // Peticion PUT para actualizar tus datos
  updateProfile(updates: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/profile`, updates, { headers: this.getAuthHeaders() });
  }
}