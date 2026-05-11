import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Notif {
  mensaje: string;
  tipo: 'error' | 'warn' | 'ok';
}

@Injectable({ providedIn: 'root' })
export class NotificationService {
  notif$ = new BehaviorSubject<Notif | null>(null);

  mostrar(mensaje: string, tipo: 'error' | 'warn' | 'ok' = 'error') {
    this.notif$.next({ mensaje, tipo });
    setTimeout(() => this.notif$.next(null), 5000);
  }
}
