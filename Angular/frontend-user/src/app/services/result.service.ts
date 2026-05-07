import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { MatchResult, Stats } from '../models/result.model';

@Injectable({ providedIn: 'root' })
export class ResultService {
  private apiUrl = 'http://localhost:8080/api/results';

  constructor(private http: HttpClient) {}

  submitResult(data: { reservationId: string; winnerId?: string; score: string; comments?: string }): Observable<MatchResult> {
    return this.http.post<MatchResult>(this.apiUrl, data);
  }

  getUserResults(userId: string): Observable<MatchResult[]> {
    return this.http.get<MatchResult[]>(`${this.apiUrl}/history/${userId}`);
  }

  // devuelve partidas jugadas, ganadas, perdidas y % victoria
  getUserStats(userId: string): Observable<Stats> {
    return this.http.get<Stats>(`${this.apiUrl}/stats/${userId}`);
  }
}
