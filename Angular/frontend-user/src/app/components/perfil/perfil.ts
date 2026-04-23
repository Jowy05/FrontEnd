import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
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
  // Hoja de personaje inicial vacia
  userProfile: User = {
    name: '',
    email: '',
    age: 0,
    sex: '',
    level: 'BASIC'
  };

  mensaje: string = '';

  constructor(private userService: UserService) {}

  // Se ejecuta nada mas cargar la pagina
  ngOnInit() {
    this.cargarPerfil();
  }

  cargarPerfil() {
    this.userService.getProfile().subscribe({
      next: (data) => {
        this.userProfile = data;
      },
      error: (err) => {
        console.error('Error cargando perfil:', err);
        this.mensaje = 'Error al cargar los datos. (Normal si MongoDB está apagado)';
      }
    });
  }

  guardarCambios() {
    this.userService.updateProfile(this.userProfile).subscribe({
      next: (updatedUser) => {
        this.userProfile = updatedUser;
        this.mensaje = '¡Estadísticas actualizadas con éxito!';
      },
      error: (err) => {
        console.error('Error al guardar:', err);
        this.mensaje = 'Fallo al actualizar el perfil.';
      }
    });
  }
}