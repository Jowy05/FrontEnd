import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {
  // Inicializamos la estructura exacta que pide tu backend
  registerData: User = {
    name: '',
    email: '',
    password: '',
    age: 18,
    sex: '',
    level: 'BASIC' // Valor por defecto
  };

  constructor(private authService: AuthService, private router: Router) {}

  onRegister() {
    this.authService.register(this.registerData).subscribe({
      next: (response) => {
        console.log('Registro y login automático exitoso');
        this.router.navigate(['/dashboard']); // Redirige al panel principal
      },
      error: (err) => {
        console.error('Fallo en el registro', err);
        alert('Error al registrar. Revisa que el email no exista ya.');
      }
    });
  }
}