import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs'
import { Empleado } from './app.model'


@Injectable({
  providedIn: 'root'
})
export class DataService {
  // Usamos mensajes para mostrar el resultado de la operacion
  private mensaje = new BehaviorSubject('Nada'); // hay que inicializarlo
  mensajeActual = this.mensaje.asObservable(); // Lo exponemos como un observable

  private empleado = new BehaviorSubject(this.getEmpleadoVacio());
  empleadoActual = this.empleado.asObservable();

  // Usamos esta variable para indicar si hay que mostrar o no el mensaje
  private mostrarMensaje = new BehaviorSubject<boolean>(false);
  mostrarMensajeActual = this.mostrarMensaje.asObservable();
  constructor() { }
  // Para actualizar mensaje
  cambiarMensaje(mensaje: string) {
    this.mensaje.next(mensaje);
  }
  cambiarMostrarMensaje(valor: boolean) {
    this.mostrarMensaje.next(valor);
  }
  cambiarEmpleado(emp : Empleado){
    this.empleado.next(emp);
  }

  getEmpleadoVacio() : Empleado{ // Igual esto mejor
    return {nif:"", pais:""};
  }
}
