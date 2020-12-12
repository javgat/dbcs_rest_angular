import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Configuracionpc, Empleado, MensajeLogin } from './app.model';
import { BehaviorSubject, Observable } from 'rxjs';
import { Config } from 'protractor';
@Injectable({ providedIn: 'root' })
export class ClienteApiRestService {

  private headersEmpty = new HttpHeaders({
    'Content-Type': 'application/json',
    Authorization: ''
  })
  private headers = new BehaviorSubject(this.headersEmpty);
  headersObs = this.headers.asObservable();

  cambiarAuthorization(nif: string, pass: string) {
    console.log(nif+':'+pass);
    let encode = btoa(nif+':'+pass);
    console.log(encode)
    this.headers.next(new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: encode
    }))
  }

  vaciarPassword(){
    this.headers.next(this.headersEmpty);
  }

  private static readonly BASE_URI = 'http://localhost:8080/TiendaPCsBackend/webresources';
  constructor(private http: HttpClient) { }

  getLogin(id: String, pass: String): Observable<HttpResponse<MensajeLogin>> {
    let url = ClienteApiRestService.BASE_URI + '/login/' + id;    
    let encode = btoa(pass as string);
    return this.http.get<MensajeLogin>(url, {observe: 'response',
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
         Authorization: encode
      })});
  }

  getEmpleado(nif: String): Observable<HttpResponse<Empleado>>{
    let url = ClienteApiRestService.BASE_URI + '/empleado/'+nif;
    return this.http.get<Empleado>(url, {observe : 'response', headers: this.headers.value});
  }

  getAllConfiguracionpc() : Observable<HttpResponse<Configuracionpc[]>>{
    let url = ClienteApiRestService.BASE_URI + '/configuracion';
    return this.http.get<Configuracionpc[]>(url, {observe: 'response', headers: this.headers.value});
  }

  borrarConfiguracion(id : Number) : Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI + '/configuracion/'+id;
    return this.http.delete<any>(url, {observe: 'response', headers: this.headers.value});
  }

  addConfiguracionpc(conf : Configuracionpc) : Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI + '/configuracion';
    return this.http.post<Configuracionpc>(url, conf, {observe: 'response', headers: this.headers.value})
  }

  modificarConfiguracionpc(conf : Configuracionpc) : Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI + '/configuracion/'+conf.idconfiguracion;
    return this.http.put<Configuracionpc>(url, conf, {observe: 'response', headers: this.headers.value})
  }
  
}