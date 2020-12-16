import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Empleado, Mensaje, Tipo } from './app.model';
import { ClienteApiRestService } from './cliente-api-rest.service';
import { DataService } from './data.service';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private autenticado = new BehaviorSubject(false);
  autenticadoObs = this.autenticado.asObservable();


  private empleado = new BehaviorSubject(new Empleado());
  empleadoActual = this.empleado.asObservable();

  private factor = new BehaviorSubject<number>(1);
  factorObs = this.factor.asObservable();

  private code = new BehaviorSubject("EUR");
  codeObs = this.code.asObservable();

  constructor() { }

  cambiarCode(code: string){
    this.code.next(code);
  }

  cambiarEmpleado(emp: Empleado) {
    this.empleado.next(emp);
  }

  cambiarEmpleadoNif(nif: String) {
    this.empleado.next(new Empleado(nif, this.empleado.value.pais));
  }

  cambiarEmpleadoPais(pais: String) {
    this.empleado.next(new Empleado(this.empleado.value.nif, pais));
  }

  cambiarAutenticado(auth: boolean) {
    this.autenticado.next(auth);
  }


  deauthenticate() {
    this.autenticado.next(false);
    this.empleado.next(new Empleado());
  }

  cambiarFactor(valor: number) {
    this.factor.next(valor);
  }

  logout(datos: DataService, clienteApiRest : ClienteApiRestService) {
    this.deauthenticate();
    this.factor.next(1);
    this.code.next("EUR");
    clienteApiRest.vaciarPassword();
    datos.cambiarMensaje(new Mensaje("Sesion cerrada con exito", Tipo.INFO, true));
  }
}
