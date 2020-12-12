import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Configuracionpc, Empleado, Mensaje, Tipo } from '../shared/app.model';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { DataService } from '../shared/data.service';
import { SessionService } from '../shared/session.service';

@Component({
  selector: 'app-modificar-configuracion',
  templateUrl: './modificar-configuracion.component.html',
  styleUrls: ['./modificar-configuracion.component.css']
})
export class ModificarConfiguracionComponent implements OnInit {

  configuracion: Configuracionpc = {
    idconfiguracion: 0,
    tipocpu: "",
    velocidadcpu: 0,
    capacidadram: 0,
    capacidaddd: 0,
    velocidadtarjetagrafica: 0,
    memoriatarjetagrafica: 0,
    precio: 0
  };
  empleado: Empleado;
  mensaje: Mensaje;

  constructor(private ruta: ActivatedRoute, private router: Router, private clienteApiRest: ClienteApiRestService,
    private datos: DataService, private session: SessionService) {
    this.empleado = new Empleado();
    this.mensaje = new Mensaje();
  }

  ngOnInit(): void {
    this.datos.borrarMensaje();
    this.session.empleadoActual.subscribe(
      valor => this.empleado = valor
    )
    this.datos.mensajeActual.subscribe(
      mens => this.mensaje = mens
    )
    this.session.autenticadoObs.subscribe(
      auth => {
        if (!auth)
          this.redirectLogin()
      }
    )
    this.ruta.paramMap.subscribe(
      params => {
        let id = params.get('id');
        if(id == null){
          this.configuracion.idconfiguracion = 0;
        }else
          this.configuracion.idconfiguracion = Number(id);
      },
      err => console.log("Error al leer id para editar: " + err)
    )
  }

  redirectLogin() {
    this.datos.cambiarMensaje(new Mensaje("Operacion no permitida, inicia sesion con una cuenta para acceder", Tipo.ERROR, true));
    this.router.navigate(['login']);
  }

  onSubmit() {
    console.log("Enviando modificacion de la configuracion...");
    this.clienteApiRest.modificarConfiguracionpc(this.configuracion).subscribe(
      resp => {
        if (resp.status < 400) {
          this.datos.cambiarMensaje(new Mensaje("Exito al modificar la configuracion", Tipo.SUCCESS, true));
          console.log("Configuracion modificada");
          this.router.navigate(['catalogo']);
        } else {
          this.datos.cambiarMensaje(new Mensaje("Error al modificar la configuracion", Tipo.ERROR, true));
          console.log("Error al modificar la configuracion");
        }
      },
      err => {
        this.datos.cambiarMensaje(new Mensaje("Error al modificar la configuracion", Tipo.ERROR, true));
        console.log("Error en modificar la configuracion: " + err.message);
        throw err;
      }
    );
  }

  logout(){
    this.session.logout(this.datos, this.clienteApiRest);
  }

}
