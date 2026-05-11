import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ResultService } from '../../services/result.service';
import { AuthService } from '../../services/auth.service';
import { MatchResult, Stats } from '../../models/result.model';

@Component({
  selector: 'app-historial',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './historial.html',
  styleUrl: './historial.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class HistorialComponent implements OnInit {
  resultados: MatchResult[] = [];
  stats: Stats | null = null;
  cargando = true;
  mensaje = '';

  constructor(
    private resultService: ResultService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() { this.cargar(); }

  cargar() {
    const userId = this.authService.getUserId();
    if (!userId) {
      this.mensaje = 'Sesión inválida.';
      this.cargando = false;
      return;
    }

    this.resultService.getUserResults(userId).subscribe({
      next: (data) => { this.resultados = data; this.cargando = false; this.cdr.markForCheck(); },
      error: () => { this.mensaje = 'Error al cargar historial.'; this.cargando = false; this.cdr.markForCheck(); }
    });

    this.resultService.getUserStats(userId).subscribe({
      next: (data) => { this.stats = data; this.cdr.markForCheck(); },
      error: () => {}
    });
  }

  // el id del ganador tiene que coincidir con el del usuario
  fueGanador(res: MatchResult): boolean {
    return res.winner?.id === this.authService.getUserId();
  }

  irAReservas() { this.router.navigate(['/reservas']); }
  volver() { this.router.navigate(['/perfil']); }
  logout() { this.authService.logout(); this.router.navigate(['/login']); }
}