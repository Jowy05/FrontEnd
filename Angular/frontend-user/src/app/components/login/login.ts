import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms'; // Necesario para que funcione el 'ngModel' del HTML
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterLink], // Si no pones FormsModule aqui, el HTML da error de 'ngModel'
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {
  // Definimos el objeto que el HTML esta buscando
  loginData = {
    username: '',
    password: ''
  };

  constructor(private authService: AuthService, private router: Router) {}

  // Definimos la funcion que se dispara al pulsar el boton de entrar
  onLogin() {
    // Llamamos al servicio para validar al usuario con ROL USER [cite: 8]
    this.authService.login(this.loginData).subscribe({
      next: (response) => {
        console.log('Token guardado:', response.token);
        this.router.navigate(['/dashboard']); // Vamos a las estadisticas personales [cite: 19]
      },
      error: (err) => {
        alert('Credenciales incorrectas');
      }
    });
  }
}