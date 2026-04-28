import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  loginData = { email: '', password: '' };
  mensajeError = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  onLogin() {
    this.mensajeError = '';
    this.authService.login(this.loginData).subscribe({
      next: () => this.router.navigate(['/perfil']),
      error: (err) => {
        this.mensajeError = err.status === 0 
          ? 'Fallo de conexión: ¿Está el servidor Java encendido?' 
          : 'Credenciales incorrectas.';
      }
    });
  }

  irARegistro() {
    this.router.navigate(['/register']);
  }
}