import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-perfil',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './perfil.html',
  styleUrl: './perfil.css'
})
export class PerfilComponent implements OnInit {
  userProfile: User = { name: '', email: '', age: 0, sex: '', level: 'BASIC' };
  mensaje = '';
  modoEdicion = false;

  constructor(
    private userService: UserService,
    private router: Router
  ) {}

  ngOnInit() { this.cargarPerfil(); }

  cargarPerfil() {
    this.userService.getProfile().subscribe({
      next: (data) => this.userProfile = data,
      error: () => this.mensaje = 'Error al cargar perfil'
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
    this.cargarPerfil(); // Recarga datos originales por si cambió algo
    this.mensaje = '';
  }

  irAReservas() { this.router.navigate(['/reservas']); }
  irAHistorial() { this.router.navigate(['/historial']); }
  
  logout() {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }
}