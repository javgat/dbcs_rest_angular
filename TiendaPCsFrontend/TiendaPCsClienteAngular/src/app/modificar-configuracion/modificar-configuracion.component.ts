import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Configuracionpc, Empleado } from '../shared/app.model';
import { ClienteApiRestService } from '../shared/cliente-api-rest.service';
import { DataService } from '../shared/data.service';

@Component({
  selector: 'app-modificar-configuracion',
  templateUrl: './modificar-configuracion.component.html',
  styleUrls: ['./modificar-configuracion.component.css']
})
export class ModificarConfiguracionComponent implements OnInit {


  configuracion: Configuracionpc = {
    idconfiguracion: 0,
    tipocpu: "", // IGual cambiar a que sea un numero si asi lo ahgo en BACKEND
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
    this.ruta.paramMap.subscribe(
      params => {
        let id = params.get('id');
        if(id == null){
          this.configuracion.idconfiguracion = 0;
          //Comprobar no solo si es un numero, si no si existe en BD, y si no existe redireccionar a catalogo con mensaje de error
        }else
          this.configuracion.idconfiguracion = Number(id);
      },
      err => console.log("Error al leer id para editar: " + err)
    )
  }

  onSubmit() {
    console.log("Enviando modificacion de la configuracion...");
    this.clienteApiRest.modificarConfiguracionpc(this.configuracion);
  }

}
