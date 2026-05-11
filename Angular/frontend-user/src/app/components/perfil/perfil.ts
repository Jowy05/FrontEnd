import { Component, OnInit, ChangeDetectorRef, ChangeDetectionStrategy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './perfil.html',
  styleUrl: './perfil.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class PerfilComponent implements OnInit {
  userProfile: User = { name: '', email: '', age: 0, sex: '', level: 'BASIC' };
  mensaje = '';
  modoEdicion = false;
  cargando = true;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() { this.cargarPerfil(); }

  cargarPerfil() {
    this.cargando = true;
    this.userService.getProfile().subscribe({
      next: (data) => {
        this.userProfile = data;
        this.cargando = false;
        this.cdr.markForCheck();
      },
      error: () => {
        this.mensaje = 'Error al cargar perfil';
        this.cargando = false;
        this.cdr.markForCheck();
      }
    });
  }

  guardarCambios() {
    this.userService.updateProfile(this.userProfile).subscribe({
      next: (updated) => {
        this.userProfile = updated;
        this.modoEdicion = false;
        this.mensaje = 'Datos actualizados.';
        setTimeout(() => this.mensaje = '', 3000);
      },
      error: () => this.mensaje = 'Error al guardar.'
    });
  }

  activarEdicion() {
    this.modoEdicion = true;
    this.mensaje = '';
  }

  cancelarEdicion() {
    this.modoEdicion = false;
    this.cargarPerfil();
    this.mensaje = '';
  }

  irAReservas() { this.router.navigate(['/reservas']); }
  irAHistorial() { this.router.navigate(['/historial']); }
  
  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}