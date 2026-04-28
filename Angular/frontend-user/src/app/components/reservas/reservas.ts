import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ReservationService } from '../../services/reservation.service';
import { AuthService } from '../../services/auth.service';
import { Reservation } from '../../models/reservation.model';

@Component({
  selector: 'app-reservas',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './reservas.html',
  styleUrl: './reservas.css'
})
export class ReservasComponent implements OnInit {
  reservationData = { date: '', sheetNumber: 1 };
  formMensaje = '';
  formLoading = false;
  misReservas: Reservation[] = [];
  listaMensaje = '';
  listaLoading = true;

  constructor(
    private reservationService: ReservationService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() { this.cargarReservas(); }

  crearReserva() {
    this.formMensaje = '';
    this.formLoading = true;

    this.reservationService.createReservation(this.reservationData).subscribe({
      next: (res: Reservation) => {
        this.formLoading = false;
        this.formMensaje = res.status === 'CONFIRMADA' ? '¡Partida confirmada!' : 'Reserva creada. Esperando rival...';
        this.cargarReservas();
        this.reservationData = { date: '', sheetNumber: 1 };
      },
      error: () => {
        this.formLoading = false;
        this.formMensaje = 'Error al crear la reserva.';
      }
    });
  }

  cargarReservas() {
    const userId = this.authService.getUserId();
    if (!userId) {
      this.listaMensaje = 'Sesión inválida.';
      this.listaLoading = false;
      return;
    }

    this.reservationService.getUserReservations(userId).subscribe({
      next: (data: Reservation[]) => {
        this.misReservas = data;
        this.listaLoading = false;
      },
      error: () => {
        this.listaMensaje = 'Error al cargar reservas.';
        this.listaLoading = false;
      }
    });
  }

  getStatusClass(status: string): string {
    const map: Record<string, string> = {
      PENDIENTE: 'pendiente',
      CONFIRMADA: 'confirmada',
      FINALIZADA: 'finalizada',
      CANCELADA: 'cancelada'
    };
    return map[status] || '';
  }

  volver() {
    this.router.navigate(['/perfil']);
  }
}