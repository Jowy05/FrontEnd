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
  registerData: User = {
    name: '',
    email: '',
    password: '',
    age: 18,
    sex: '',
    level: 'BASIC'
  };

  constructor(private authService: AuthService, private router: Router) {}

  onRegister() {
    this.authService.register(this.registerData).subscribe({
      next: () => {
        this.router.navigate(['/perfil']);
      },
      error: () => {
        alert('Error al registrar. Comprueba que el email no esté en uso.');
      }
    });
  }
}