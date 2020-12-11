import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Configuracionpc, Empleado, Mensaje, Tipo } from '../shared/app.model';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { DataService } from '../shared/data.service';
import { SessionService } from '../shared/session.service';

@Component({
  selector: 'app-nueva-configuracion',
  templateUrl: './nueva-configuracion.component.html',
  styleUrls: ['./nueva-configuracion.component.css']
})
export class NuevaConfiguracionComponent implements OnInit {

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
  factor: number;
  constructor(private ruta: ActivatedRoute, private router: Router, private clienteApiRest: ClienteApiRestService,
    private datos: DataService, private session: SessionService) {
    this.empleado = new Empleado();
    this.mensaje = new Mensaje();
    this.factor = 1;
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
    this.session.factorObs.subscribe(
      factor => {
        this.factor = factor;
      }
    )
  }

  redirectLogin() {
    this.datos.cambiarMensaje(new Mensaje("Operacion no permitida, inicia sesion con una cuenta para acceder", Tipo.ERROR, true));
    this.router.navigate(['login']);
  }

  onSubmit() {

    this.configuracion.precio = this.configuracion.precio as number * this.factor;
    console.log("Enviando la configuracion");
    this.clienteApiRest.addConfiguracionpc(this.configuracion).subscribe(
      resp => {
        if (resp.status < 400) {
          this.datos.cambiarMensaje(new Mensaje("Exito al crear nueva configuracion", Tipo.SUCCESS, true));
          console.log("Nueva configuracion creada");
          this.router.navigate(['catalogo']);
        } else {
          this.datos.cambiarMensaje(new Mensaje("Error al crear nueva configuracion", Tipo.ERROR, true));
          console.log("Error al crear nueva configuracion");
        }
      },
      err => {
        this.datos.cambiarMensaje(new Mensaje("Error al crear nueva configuracion", Tipo.ERROR, true));
        console.log("Error en crear configuracion: " + err.message);
        throw err;
      }
    );
  }

  logout() {
    this.session.logout(this.datos);
  }

}
