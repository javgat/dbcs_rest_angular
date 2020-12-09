import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router'; 


import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { EmpleadoLogin, Empleado} from '../shared/app.model';
import { DataService } from '../shared/data.service';

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

  mostrarMensaje: boolean;
  mensaje: string;
  empleadoLogin = this.empleadoLoginVacio as EmpleadoLogin;

  constructor(private ruta: ActivatedRoute, private router: Router, private clienteApiRest: ClienteApiRestService, private datos: DataService) {
    this.mensaje = "";
    this.mostrarMensaje = false;
  }

  ngOnInit(): void {//Por defecto, borrarlo
    console.log("Dentro funcion ngOnInit de Login");
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
    this.clienteApiRest.getLogin(this.empleadoLogin.nif, this.empleadoLogin.password).subscribe(
      resp => {
        if(resp.status < 400){
          this.mostrarMensaje = true;
          this.mensaje = "Inicio de sesion con exito";
          // Aqui coger datos del empleado (pais)
          //this.copiaEmpleado(this.empleadoLogin.nif);
          let emp = {
            nif : this.empleadoLogin.nif,
            pais : 'Great Britain'
          } as Empleado;
          this.datos.cambiarEmpleado(emp);
          this.router.navigate(['catalogo']); // Te redirecciona pero aun no existe otro componente angular (decidir a donde redirecciona tb) -> a catalogo configs
        }else{
          // Aqui coger de la respuesta del servidor el tipo de error que da
          this.mostrarMensaje = true;
          this.mensaje = "Error al iniciar sesion, nombre y/o clave incorrectos";
        }
      },
      err => {
        console.log("Error en login: " + err.message);
        throw err;
      }
    );
  }

  copiaEmpleado(nif: String){
    this.clienteApiRest.getEmpleado(nif).subscribe(
      resp =>{
        if(resp.status < 400){
          let emp = this.datos.getEmpleadoVacio();
          if(resp.body !=null)
            emp = {
              nif : nif,
              pais : resp.body.pais
            } as Empleado;
          this.datos.cambiarEmpleado(emp);
        }else{
          console.log("Error al obtener los datos del empleado, respuesta incorrecta: "+ resp.status);
        }
      },
      err => {
        console.log("Error al obtener los datos del empleado: " + err.message);
        throw err;
      }
    )
  }

}
