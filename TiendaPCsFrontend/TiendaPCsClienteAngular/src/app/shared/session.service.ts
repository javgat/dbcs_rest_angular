import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Empleado } from './app.model';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private autenticado = new BehaviorSubject(false);
  autenticadoObs = this.autenticado.asObservable();

  
  private empleado = new BehaviorSubject(new Empleado());
  empleadoActual = this.empleado.asObservable();
  
  constructor() {  }


  cambiarEmpleado(emp : Empleado){
    this.empleado.next(emp);
  }

  cambiarEmpleadoNif(nif : String){
    this.empleado.next(new Empleado(nif, this.empleado.value.pais));
  }

  cambiarEmpleadoPais(pais : String){
    this.empleado.next(new Empleado(this.empleado.value.nif, pais));
  }

  cambiarAutenticado(auth: boolean){
    this.autenticado.next(auth);
  }
}
