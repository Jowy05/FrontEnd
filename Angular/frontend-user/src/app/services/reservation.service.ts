import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reservation } from '../models/reservation.model';

@Injectable({ providedIn: 'root' })
export class ReservationService {
  private apiUrl = 'http://localhost:8080/api/reservations';

  constructor(private http: HttpClient) {}

  createReservation(data: { date: string; sheetNumber: number; player1Id: string }): Observable<Reservation> {
    return this.http.post<Reservation>(this.apiUrl, data);
  }

  getUserReservations(userId: string): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.apiUrl}/history/${userId}`);
  }

  // devuelve las reservas que ya hay en esa fecha para saber qué pistas están libres
  getDisponibilidad(date: string): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.apiUrl}/availability`, { params: { date } });
  }
}