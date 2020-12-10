import { computeDecimalDigest } from '@angular/compiler/src/i18n/digest';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Subject } from 'rxjs';
import { Empleado, Mensaje, Tipo } from './app.model';
import { DataService } from './data.service';
import { FrankfurterService } from './frankfurter.service';
import { RestCountriesService } from './rest-countries.service';

@Injectable({
  providedIn: 'root'
})
export class PrecioServiceService {


  private factor = new BehaviorSubject<number>(1);
  factorObs = this.factor.asObservable();

  constructor(private restCS: RestCountriesService, private frankS: FrankfurterService, private datos: DataService) {}

  cambiarFactor(valor: number){
    this.factor.next(valor);
  }


}
