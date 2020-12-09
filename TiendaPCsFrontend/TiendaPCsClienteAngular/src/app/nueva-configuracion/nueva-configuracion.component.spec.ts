import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NuevaConfiguracionComponent } from './nueva-configuracion.component';

describe('NuevaConfiguracionComponent', () => {
  let component: NuevaConfiguracionComponent;
  let fixture: ComponentFixture<NuevaConfiguracionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NuevaConfiguracionComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(NuevaConfiguracionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
