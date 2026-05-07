import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login';
import { RegisterComponent } from './components/register/register';
import { PerfilComponent } from './components/perfil/perfil';
import { ReservasComponent } from './components/reservas/reservas';
import { HistorialComponent } from './components/historial/historial';
import { MatchResult } from './components/match-result/match-result';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'perfil', component: PerfilComponent, canActivate: [authGuard] },
  { path: 'reservas', component: ReservasComponent, canActivate: [authGuard] },
  { path: 'historial', component: HistorialComponent, canActivate: [authGuard] },
  { path: 'resultado/:id', component: MatchResult, canActivate: [authGuard] },
  { path: '**', redirectTo: 'login' }
];