import { User } from './user.model';

export interface MatchResult {
  id?: string;
  reservationId: string;
  winner?: User;
  score: string;
  comments?: string;
  recordedAt?: string;
}

export interface Stats {
  played: number;
  wins: number;
  losses: number;
  winRate: number;
}
