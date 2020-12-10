import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';


import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { EmpleadoLogin, Empleado, Mensaje, Tipo, MensajeLogin } from '../shared/app.model';
import { DataService } from '../shared/data.service';
import { HttpErrorResponse } from '@angular/common/http';
import { SessionService } from '../shared/session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  empleadoLoginVacio = {
    nif: "",
    password: ""
  }

  mensaje: Mensaje;
  empleadoLogin = this.empleadoLoginVacio as EmpleadoLogin;

  constructor(private ruta: ActivatedRoute, private router: Router, private clienteApiRest: ClienteApiRestService,
    private datos: DataService, private session: SessionService) {
    this.mensaje = new Mensaje();
  }

  ngOnInit(): void {
    this.datos.mensajeActual.subscribe(
      valor => this.mensaje = valor
    )
    console.log("Valor actual del mensaje: " + this.mensaje.texto);

  }

  loginSubmit() {
    console.log("Enviando el formulario");
    this.clienteApiRest.getLogin(this.empleadoLogin.nif, this.empleadoLogin.password).subscribe(
      resp => {
        if (resp.status < 400) {
          console.log("Respuesta correcta del servidor:"+ resp.body);
          this.datos.cambiarMensaje(new Mensaje(resp.body?.mensaje || "Inicio de sesion con exito", Tipo.SUCCESS, true));
          // Aqui coger datos del empleado (pais)
          this.copiaEmpleado(this.empleadoLogin.nif);
        } else {
          // Aqui coger de la respuesta del servidor el tipo de error que da
          console.log("Mensaje de error recibido del servidor");
          let newMsn = new Mensaje(resp.body?.mensaje || "No se recibio mensaje del servidor", Tipo.ERROR, true);
          this.datos.cambiarMensaje(newMsn);
        }
      },
      (err : HttpErrorResponse) => {
        if(err.status < 500){
          this.datos.cambiarMensaje(new Mensaje(err.error.mensaje, Tipo.ERROR, true));
        }else{
          console.log(err.status+ " body: "+ err.error.mensaje);
          this.datos.cambiarMensaje(new Mensaje("No se pudo conectar con el servidor", Tipo.ERROR, true));
          console.log("Error en login: " + err.message);
          throw err;
        }
      }
    );
  }

  copiaEmpleado(nif: String) {
    this.clienteApiRest.getEmpleado(nif).subscribe(
      resp => {
        if (resp.status < 400) {
          let emp = new Empleado();
          if (resp.body != null)
            emp = new Empleado(nif, resp.body.pais);
          this.session.cambiarEmpleado(emp);
          this.session.cambiarAutenticado(true);
          console.log("Redireccionando al catalogo...");
          this.router.navigate(['catalogo']);
        } else {
          // Aqui no modificamos el mensaje, si sucede esto es porque hay algun error en el cliente o en servidor, no porque esten mal los datos introducidos
          console.log("Error al obtener los datos del empleado, respuesta incorrecta: " + resp.status);
        }
      },
      err => {
        console.log("Error al obtener los datos del empleado: " + err.message);
        throw err;
      }
    )
  }

}
