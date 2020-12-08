import { Component, OnInit } from '@angular/core';
import { Configuracionpc } from '../shared/app.model';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { DataService } from '../shared/data.service';

@Component({
  selector: 'app-catalogo',
  templateUrl: './catalogo.component.html',
  styleUrls: ['./catalogo.component.css']
})
export class CatalogoComponent implements OnInit {

  configs: Configuracionpc[];
  mensaje: String;
  mostrarMensaje: boolean;
  constructor(private clienteApiRest: ClienteApiRestService, private datos: DataService) {
    this.configs = [];
    this.mensaje = "";
    this.mostrarMensaje = false;
  }

  ngOnInit() {
    this.getConfigs_AccesoResponse();
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
