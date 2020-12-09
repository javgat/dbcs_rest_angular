import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Configuracionpc, Empleado } from '../shared/app.model';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { DataService } from '../shared/data.service';

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

  constructor(private ruta: ActivatedRoute, private router: Router, private clienteApiRest: ClienteApiRestService, private datos: DataService) {
    this.empleado = this.datos.getEmpleadoVacio();
    this.datos.empleadoActual.subscribe(
      valor => this.empleado = valor
    )
  }

  ngOnInit(): void {

  }

  onSubmit() {
    console.log("Enviando la configuracion");
    this.clienteApiRest.addConfiguracionpc(this.configuracion).subscribe(
      resp => {
        if (resp.status < 400) {
          //this.mostrarMensaje = true;
          //this.mensaje = "Exito al crear nueva config";
          console.log("Nueva configuracion creada");
          this.router.navigate(['catalogo']); // Te redirecciona pero aun no existe otro componente angular (decidir a donde redirecciona tb) -> a catalogo configs
        } else {
          // Aqui coger de la respuesta del servidor el tipo de error que da
          console.log("Error al crear nueva configuracion");
        }
      },
      err => {
        console.log("Error en crear configuracion: " + err.message);
        throw err;
      }
    );
    //igual redireccione si o si?
  }

}
