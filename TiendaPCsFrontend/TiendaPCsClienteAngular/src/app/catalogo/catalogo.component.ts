import { R3FactoryDelegateType } from '@angular/compiler/src/render3/r3_factory';
import { Component, OnInit } from '@angular/core';
import { Configuracionpc, Empleado, Currency } from '../shared/app.model';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { RestCountriesService } from '../shared/rest-countries.service'
import { FrankfurterService } from '../shared/frankfurter.service'
import { DataService } from '../shared/data.service';
import { Observable, BehaviorSubject } from 'rxjs';

@Component({
  selector: 'app-catalogo',
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent implements OnInit {

  configs: Configuracionpc[];
  mensaje: String;
  mostrarMensaje: boolean;
  empleado: Empleado;

  empleadoVacio = {
    nif:"",
    pais:""
  } as Empleado;

  constructor(private clienteApiRest: ClienteApiRestService, private restCS: RestCountriesService,
     private frankS: FrankfurterService, private datos: DataService) {
    this.configs = [];
    this.mensaje = "";
    this.mostrarMensaje = false;
    
    this.empleado = this.datos.empleadoVacio; //esto se puede hacer?? no se typescript
    console.log("Empleado: "+this.empleado.nif+", pais: "+this.empleado.pais);
  }

  ngOnInit() {
    this.datos.empleadoActual.subscribe(
      valor => this.empleado = valor
    )
    console.log("Empleado: "+this.empleado.nif+", pais: "+this.empleado.pais);
    this.getConfigs_AccesoResponse();
  }

  configsPrecioEmpleado(emp: Empleado){
    let code = "EUR" as String;
    // llamo rest-countries con emp.pais
    this.restCS.getCodeCoin(emp.pais).subscribe(
      resp => {
        if(resp.status < 400){
          if(resp.body != null){
            code = resp.body[0].currencies[0].code;
            this.frankS.getFactor(code).subscribe(
              resp => {
                if(resp.status < 400){   
                  // cojo factor de conversion de frankfurter
                  let factor: number = 1; // Factor de division
                  factor = resp.body.rates.EUR;                           
                  for(let conf of this.configs){
                    conf.precio = conf.precio as number / factor;
                  }
                }else{
                  this.mensaje = 'Error al acceder a los datos de conversion de monedas';
                  this.mostrarMensaje = true;
                }
              },
              err => {
                console.log("Error al acceder a Frankfurter Service: " + err.message);
                throw err;
              }
            )
          }else
            console.log("Respuesta vacia");
        }else{
          this.mensaje = 'Error al acceder a los datos de pais';
          this.mostrarMensaje = true;
        }
      },
      err => {
        console.log("Error al acceder a Rest Countries Service: " + err.message);
        throw err;
      }
    )
    
  }

  getConfigs_AccesoResponse() {
    this.clienteApiRest.getAllConfiguracionpc().subscribe(
      resp => {
        console.log("Cabeceras: " + resp.headers.keys());
        console.log("Status: " + resp.status);
        if (resp.status < 400) {
          this.configs = resp.body || []; // se accede al cuerpo de la respuesta
        } else {
          this.mensaje = 'Error al acceder a los datos';
          this.mostrarMensaje = true;
        }
      },
      err => {
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
          this.mostrarMensaje = true; // actualizamos variable compartida
          this.mensaje = "Registro borrado con exito"; // actualizamos variable compartida
          this.getConfigs_AccesoResponse(); //Actualizamos la lista de configs en la vista
        } else {
          this.mostrarMensaje = true;
          this.mensaje = "Error al eliminar registro";
        }
      },
      err => {
        console.log("Error al borrar: " + err.message);
        throw err;
      }
    )
  }

}
