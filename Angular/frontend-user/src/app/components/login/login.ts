import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  loginData = {
    email: '',
    password: ''
  };

  // Variable para guardar el texto del error
  mensajeError: string = ''; 

  constructor(private authService: AuthService, private router: Router) {}

  onLogin() {
    // Limpiamos el error antes de un nuevo intento
    this.mensajeError = ''; 

    this.authService.login(this.loginData).subscribe({
      next: (response) => {
        console.log('Login exitoso', response);
        // De momento lo mandamos a perfil para que veas que funciona
        this.router.navigate(['/perfil']); 
      },
      error: (err) => {
        console.error('Error capturado:', err);
        // Si el status es 0, significa que Angular ni siquiera pudo llegar al Java
        if (err.status === 0) {
          this.mensajeError = 'Fallo de conexión: ¿Está el servidor Java encendido?';
        } else {
          // Para errores 401, 403, etc.
          this.mensajeError = 'Credenciales incorrectas o usuario no encontrado.';
        }
      }
    });
  }
}