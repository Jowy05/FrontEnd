import { Routes } from '@angular/router';

// Importamos usando TUS nombres de archivo exactos
import { LoginComponent } from './components/login/login';
import { RegisterComponent } from './components/register/register';
import { PerfilComponent } from './components/perfil/perfil';
import { ReservasComponent } from './components/reservas/reservas';
import { HistorialComponent } from './components/historial/historial';
import { DashboardComponent } from './components/dashboard/dashboard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'perfil', component: PerfilComponent }, // Para visualización y edición [cite: 12]
  { path: 'reservas', component: ReservasComponent }, // Para creación de reservas [cite: 13]
  { path: 'historial', component: HistorialComponent }, // Para histórico y resultados [cite: 16, 17]
  { path: 'dashboard', component: DashboardComponent }, // Para el módulo de estadísticas [cite: 19]
  { path: '**', redirectTo: 'login' }
];