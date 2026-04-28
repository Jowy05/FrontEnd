import { TestBed } from '@angular/core/testing';
import { AuthService } from './auth.service'; // <-- Corregido

describe('AuthService', () => { // <-- Corregido
  let service: AuthService; // <-- Corregido

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthService); // <-- Corregido
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});