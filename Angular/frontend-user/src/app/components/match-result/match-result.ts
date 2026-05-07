import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ResultService } from '../../services/result.service';
import { Reservation } from '../../models/reservation.model';

@Component({
  selector: 'app-match-result',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './match-result.html',
  styleUrl: './match-result.css',
})
export class MatchResult implements OnInit {
  reservation: Reservation | null = null;
  resultData = { winnerId: '', score: '', comments: '' };
  mensaje = '';
  cargando = false;

  constructor(
    private resultService: ResultService,
    private router: Router
  ) {}

  ngOnInit() {
    // recoge los datos de la reserva que pasamos desde la página de reservas
    this.reservation = history.state.reservation || null;
    if (!this.reservation) {
      this.router.navigate(['/reservas']);
    }
  }

  submitResult() {
    if (!this.reservation?.id || !this.resultData.score) return;
    this.cargando = true;
    this.mensaje = '';

    const data: any = {
      reservationId: this.reservation.id,
      score: this.resultData.score,
    };
    if (this.resultData.winnerId) data.winnerId = this.resultData.winnerId;
    if (this.resultData.comments) data.comments = this.resultData.comments;

    this.resultService.submitResult(data).subscribe({
      next: () => {
        this.cargando = false;
        this.mensaje = '¡Resultado registrado!';
        setTimeout(() => this.router.navigate(['/historial']), 1500);
      },
      error: () => {
        this.cargando = false;
        this.mensaje = 'Error al registrar resultado.';
      }
    });
  }

  volver() { this.router.navigate(['/reservas']); }
}
