import { Component, OnInit } from '@angular/core';

import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { Usuario} from '../shared/app.model';
import { DataService } from '../shared/data.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  usuarioVacio = {
    nif: "",
    password: ""
  }

  mostrarMensaje: boolean;
  mensaje: string;
  usuario = this.usuarioVacio as Usuario;

  constructor( private clienteApiRest: ClienteApiRestService, private datos: DataService) {
    this.mensaje = "";
    this.mostrarMensaje = false;
  }

  ngOnInit(): void {
    console.log("Dentro funcion ngOnInit de Listar");
    // capturamos valor de mostrarMensaje. Recordar que la variable es un Observable
    this.datos.mostrarMensajeActual.subscribe(
      valor => this.mostrarMensaje = valor
    )
    console.log("Valor actual de si mostrar mensaje: " + this.mostrarMensaje);
    // capturamos el valor de mensaje
    this.datos.mensajeActual.subscribe(
      valor => this.mensaje = valor
    )
    console.log("Valor actual del mensaje: " + this.mensaje);
    
  }

  loginSubmit(){
    console.log("Enviando el formulario");
    this.clienteApiRest.getLogin(this.usuario.nif, this.usuario.password).subscribe(
      resp => {
        if(resp.status < 400){
          this.mostrarMensaje = true;
          this.mensaje = "Inicio de sesion con exito";
        }else{
          this.mostrarMensaje = true;
          this.mensaje = "Error al iniciar sesion";
        }
      },
      err => {
        console.log("Error en login: " + err.message);
        throw err;
      }
    );
  }

}
