import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs'
import { Empleado, Mensaje, Tipo } from './app.model'


@Injectable({
  providedIn: 'root'
})
export class DataService {
  // Usamos mensajes para mostrar el resultado de la operacion
  private mensaje = new BehaviorSubject<Mensaje>(new Mensaje()); // hay que inicializarlo
  mensajeActual = this.mensaje.asObservable(); // Lo exponemos como un observable

  constructor() { }

  // Para actualizar mensaje
  cambiarMensaje(mensaje: Mensaje){
    this.mensaje.next(mensaje);
  }

  cambiarTextoMensaje(texto: string) {
    let oldMsn = this.mensaje.value;
    this.mensaje.next(new Mensaje(texto, oldMsn.type, oldMsn.mostrar));
  }

  cambiarMostrarMensaje(mostrar: boolean) {
    let oldMsn = this.mensaje.value;
    this.mensaje.next(new Mensaje(oldMsn.texto, oldMsn.type, mostrar));
  }

  cambiarTypeMensaje(type : Tipo){
    let oldMsn = this.mensaje.value;
    this.mensaje.next(new Mensaje(oldMsn.texto, type, oldMsn.mostrar));
  }
}
