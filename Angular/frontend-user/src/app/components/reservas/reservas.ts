import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy } from '@angular/core';
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
  styleUrl: './reservas.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ReservasComponent implements OnInit {
  reservationData = { date: '', sheetNumber: 1 };
  formMensaje = '';
  formLoading = false;
  misReservas: Reservation[] = [];
  listaMensaje = '';
  listaLoading = true;
  pistasOcupadas: number[] = [];

  constructor(
    private reservationService: ReservationService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() { this.cargarReservas(); }

  // cuando cambia la fecha consulta qué pistas están libres
  onFechaChange() {
    if (!this.reservationData.date) return;
    this.reservationService.getDisponibilidad(this.reservationData.date).subscribe({
      next: (reservas) => {
        this.pistasOcupadas = reservas.map(r => r.sheetNumber);
        // si la pista elegida quedó ocupada, coge la primera libre
        if (this.pistasOcupadas.includes(this.reservationData.sheetNumber)) {
          this.reservationData.sheetNumber = [1, 2, 3, 4].find(n => !this.pistasOcupadas.includes(n)) || 1;
        }
        this.cdr.markForCheck();
      },
      error: () => {}
    });
  }

  estaOcupada(pista: number): boolean {
    return this.pistasOcupadas.includes(pista);
  }

  crearReserva() {
    this.formMensaje = '';
    this.formLoading = true;

    const player1Id = this.authService.getUserId();
    if (!player1Id) {
      this.formMensaje = 'Sesión inválida. Vuelve a iniciar sesión.';
      this.formLoading = false;
      return;
    }

    this.reservationService.createReservation({ ...this.reservationData, player1Id }).subscribe({
      next: (res: Reservation) => {
        this.formLoading = false;
        this.formMensaje = res.status === 'CONFIRMADA' ? '¡Partida confirmada!' : 'Reserva creada. Esperando rival...';
        this.cargarReservas();
        this.reservationData = { date: '', sheetNumber: 1 };
        this.pistasOcupadas = [];
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.formLoading = false;
        if (err.status === 401 || err.status === 403) {
          this.formMensaje = 'Sesión expirada. Vuelve a iniciar sesión.';
        } else {
          this.formMensaje = 'Error al crear la reserva.';
        }
        this.cdr.markForCheck();
      }
    });
  }

  cargarReservas() {
    const userId = this.authService.getUserId();
    this.listaLoading = true;
    this.listaMensaje = '';

    if (!userId) {
      this.listaMensaje = 'Sesión inválida.';
      this.listaLoading = false;
      return;
    }

    this.reservationService.getUserReservations(userId).subscribe({
      next: (data: Reservation[]) => {
        this.misReservas = data;
        this.listaLoading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        this.listaLoading = false;
        if (err.status === 401 || err.status === 403) {
          this.listaMensaje = 'Sesión expirada. Vuelve a iniciar sesión.';
        } else {
          this.listaMensaje = 'Error al cargar reservas.';
        }
        this.cdr.markForCheck();
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

  registrarResultado(res: Reservation) {
    this.router.navigate(['/resultado', res.id], { state: { reservation: res } });
  }

  volver() { this.router.navigate(['/perfil']); }
  irAHistorial() { this.router.navigate(['/historial']); }
  logout() { this.authService.logout(); this.router.navigate(['/login']); }
}