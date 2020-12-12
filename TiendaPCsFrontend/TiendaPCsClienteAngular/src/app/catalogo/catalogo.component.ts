import { R3FactoryDelegateType } from '@angular/compiler/src/render3/r3_factory';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Configuracionpc, Empleado, Currency, Mensaje, Tipo, ArrayCurrency } from '../shared/app.model';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { RestCountriesService } from '../shared/rest-countries.service'
import { FrankfurterService } from '../shared/frankfurter.service'
import { DataService } from '../shared/data.service';
import { Observable, BehaviorSubject } from 'rxjs';
import { SessionService } from '../shared/session.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-catalogo',
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent implements OnInit {

  configs: Configuracionpc[];
  mensaje: Mensaje;
  empleado: Empleado;
  factor: number;
  code: string;

  constructor(private ruta: ActivatedRoute, private router: Router, private clienteApiRest: ClienteApiRestService, private restCS: RestCountriesService,
    private frankS: FrankfurterService, private datos: DataService, private session: SessionService) {
    this.configs = [];
    this.mensaje = new Mensaje();
    this.factor = 1;
    this.empleado = new Empleado();
    this.code = "EUR";
  }

  ngOnInit() {
    this.session.empleadoActual.subscribe(
      valor => this.empleado = valor
    )
    console.log("Empleado: " + this.empleado.nif + ", pais: " + this.empleado.pais);
    this.datos.mensajeActual.subscribe(
      mens => this.mensaje = mens
    )
    this.session.factorObs.subscribe(
      factor => {
        this.factor = factor
      }
    )
    this.session.codeObs.subscribe(
      code => {
        this.code = code
      }
    )
    this.session.autenticadoObs.subscribe(
      auth => {
        if (!auth)
          this.redirectLogin()
        else
          this.getConfigs_AccesoResponse();
      }
    )

  }

  redirectLogin() {
    this.datos.cambiarMensaje(new Mensaje("Operacion no permitida, inicia sesion con una cuenta para acceder", Tipo.ERROR, true));
    this.router.navigate(['login']);
  }

  configsPrecio() {
    for (let conf of this.configs) {
      //console.log("Precio antes: " + conf.precio);
      conf.precio = parseFloat((conf.precio as number / this.factor).toFixed(2));
      //console.log("Precio despues: " + conf.precio);
    }
  }

  getConfigs_AccesoResponse() {
    this.clienteApiRest.getAllConfiguracionpc().subscribe(
      resp => {
        if (resp.status < 400) {
          this.configs = resp.body || [];
          this.configsPrecio();
        } else {
          this.datos.cambiarMensaje(new Mensaje("Error al acceder a los datos de configuraciones", Tipo.ERROR, true));
        }
      },
      err => {
        this.datos.cambiarMensaje(new Mensaje("Error al acceder a los datos de configuraciones", Tipo.ERROR, true));
        console.log("Error al traer la lista: " + err.message);
        throw err;
      }
    )
    
  }

  borrar(id: Number) {
    this.clienteApiRest.borrarConfiguracion(id).subscribe(
      resp => {
        if (resp.status < 400) {
          this.datos.cambiarMensaje(new Mensaje("Registro borrado con exito", Tipo.SUCCESS, true));
          this.getConfigs_AccesoResponse(); //Actualizamos la lista de configs en la vista
        } else {
          this.datos.cambiarMensaje(new Mensaje("Error al eliminar registro", Tipo.ERROR, true));
        }
      },
      err => {
        this.datos.cambiarMensaje(new Mensaje("Error al eliminar registro", Tipo.ERROR, true));
        console.log("Error al borrar: " + err.message);
        throw err;
      }
    )
  }

  logout() {
    this.session.logout(this.datos, this.clienteApiRest);
  }



}
