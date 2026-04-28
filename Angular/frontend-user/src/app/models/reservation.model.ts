import { User } from './user.model';

export type ReservationStatus = 'PENDIENTE' | 'CONFIRMADA' | 'FINALIZADA' | 'CANCELADA';

export interface Reservation {
  id?: string;
  date: string;
  sheetNumber: number;
  status: ReservationStatus;
  player1: User;
  player2?: User;
}