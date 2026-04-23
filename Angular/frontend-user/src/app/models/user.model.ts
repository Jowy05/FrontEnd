export interface User {
  id?: string;
  name: string;
  email: string;
  password?: string;
  age: number;
  sex: string;
  level: 'BASIC' | 'MEDIUM' | 'HIGH';
  role?: 'USER' | 'ADMIN';
}