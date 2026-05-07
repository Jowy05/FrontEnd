import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { isPlatformBrowser } from '@angular/common';
import { User } from '../models/user.model';

@Injectable({ providedIn: 'root' })
export class UserService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  getProfile(): Observable<User> {
    // guarda el id del usuario para usarlo en reservas e historial
    return this.http.get<User>(`${this.apiUrl}/profile`).pipe(
      tap((user) => {
        if (user.id && isPlatformBrowser(this.platformId)) {
          localStorage.setItem('userId', user.id);
        }
      })
    );
  }

  updateProfile(updates: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/profile`, updates);
  }
}