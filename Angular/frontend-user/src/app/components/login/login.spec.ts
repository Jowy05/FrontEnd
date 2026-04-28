import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login'; // <-- Corregido

describe('LoginComponent', () => { // <-- Corregido
  let component: LoginComponent; // <-- Corregido
  let fixture: ComponentFixture<LoginComponent>; // <-- Corregido

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoginComponent] // <-- Corregido
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginComponent); // <-- Corregido
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});