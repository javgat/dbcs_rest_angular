import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModificarConfiguracionComponent } from './modificar-configuracion.component';

describe('ModificarConfiguracionComponent', () => {
  let component: ModificarConfiguracionComponent;
  let fixture: ComponentFixture<ModificarConfiguracionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModificarConfiguracionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ModificarConfiguracionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
