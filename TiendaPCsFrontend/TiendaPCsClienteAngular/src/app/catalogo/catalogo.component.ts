import { R3FactoryDelegateType } from '@angular/compiler/src/render3/r3_factory';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Configuracionpc, Empleado, Currency, Mensaje, Tipo } from '../shared/app.model';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { RestCountriesService } from '../shared/rest-countries.service'
import { FrankfurterService } from '../shared/frankfurter.service'
import { DataService } from '../shared/data.service';
import { Observable, BehaviorSubject } from 'rxjs';
import { Session } from 'protractor';
import { SessionService } from '../shared/session.service';

@Component({
  selector: 'app-catalogo',
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent implements OnInit {

  configs: Configuracionpc[];
  mensaje: Mensaje;
  empleado: Empleado;

  constructor(private ruta: ActivatedRoute, private router: Router, private clienteApiRest: ClienteApiRestService, private restCS: RestCountriesService,
    private frankS: FrankfurterService, private datos: DataService, private session: SessionService) {
    this.configs = [];
    this.mensaje = new Mensaje();

    this.empleado = new Empleado();
    console.log("Empleado: " + this.empleado.nif + ", pais: " + this.empleado.pais);
  }

  ngOnInit() {
    this.session.empleadoActual.subscribe(
      valor => this.empleado = valor
    )
    console.log("Empleado: " + this.empleado.nif + ", pais: " + this.empleado.pais);
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

  configsPrecioEmpleado(emp: Empleado) {
    let code = "EUR" as String;
    // llamo rest-countries con emp.pais
    this.restCS.getCodeCoin(emp.pais).subscribe(
      resp => {
        if (resp.status < 400) {
          if (resp.body != null) {
            code = resp.body[0].currencies[0].code;
            if (code != "EUR") { //Solo cambiar valores si el codigo no es EUR
              this.frankS.getFactor(code).subscribe(
                resp => {
                  if (resp.status < 400) {
                    // cojo factor de conversion (division) de frankfurter
                    let factor = resp.body.rates.EUR;
                    for (let conf of this.configs) {
                      console.log("Precio antes: " + conf.precio);
                      conf.precio = conf.precio as number / factor;
                      console.log("Precio despues: " + conf.precio);
                    }
                  } else {
                    this.datos.cambiarMensaje(new Mensaje('Error al acceder a los datos de conversion de monedas', Tipo.ERROR, true));
                  }
                },
                err => {
                  this.datos.cambiarMensaje(new Mensaje("Error al acceder a la api Frankfurter, intentelo mas tarde", Tipo.ERROR, true));
                  console.log("Error al acceder a Frankfurter Service: " + err.message);
                  throw err;
                }
              )
            }//else pues se queda igual
          } else
            console.log("Error, Respuesta vacia");
        } else {
          this.datos.cambiarMensaje(new Mensaje("Error al acceder a la api RestCountries, intentelo mas tarde", Tipo.ERROR, true));
        }
      },
      err => {
        this.datos.cambiarMensaje(new Mensaje("Error al acceder a la api RestCountries, intentelo mas tarde", Tipo.ERROR, true));
        console.log("Error al acceder a Rest Countries Service: " + err.message);
        throw err;
      }
    )

  }

  getConfigs_AccesoResponse() {
    this.clienteApiRest.getAllConfiguracionpc().subscribe(
      resp => {
        if (resp.status < 400) {
          this.configs = resp.body || [];
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
    this.configsPrecioEmpleado(this.empleado);
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

}
