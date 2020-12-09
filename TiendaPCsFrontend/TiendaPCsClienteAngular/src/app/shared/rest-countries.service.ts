import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Currency, ArrayCurrency } from './app.model';
import { Observable } from 'rxjs';
@Injectable({ providedIn: 'root' })
export class RestCountriesService {

  private static readonly BASE_URI = 'https://restcountries.eu/rest/v2';
  constructor(private http: HttpClient) { } // inyectamos el modulo HttpClientModule

  getCodeCoin(pais: String) : Observable<HttpResponse<ArrayCurrency[]>>{
    let url = RestCountriesService.BASE_URI + '/name/'+pais+'?fields=currencies';
    return this.http.get<ArrayCurrency[]>(url, {observe: 'response'});
  }
}
