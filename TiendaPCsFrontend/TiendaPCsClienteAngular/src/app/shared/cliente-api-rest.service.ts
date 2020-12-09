import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Configuracionpc, Empleado } from './app.model';
import { Observable } from 'rxjs';
import { Config } from 'protractor';
@Injectable({ providedIn: 'root' })
export class ClienteApiRestService {

  // Esta linea es dependiente de la URL de la API Rest creada. MODIFICARLA ADECUADAMENTE
  private static readonly BASE_URI = 'http://localhost:8080/TiendaPCsBackend/webresources';
  constructor(private http: HttpClient) { } // inyectamos el modulo HttpClientModule

  getLogin(id: String, pass: String): Observable<HttpResponse<String>> {
    let url = ClienteApiRestService.BASE_URI + '/login/' + id;
    // de alguna manera meter la password
    // No me deja usar el options declarado antes
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        Authorization: 'my-auth-token'
      }),
      observe: 'response'
    };
    return this.http.get<String>(url, {observe: 'response',
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
         Authorization: pass as string
      })});
  }

  getEmpleado(nif: String): Observable<HttpResponse<Empleado>>{
    let url = ClienteApiRestService.BASE_URI + '/empleado/'+nif;
    return this.http.get<Empleado>(url, {observe : 'response'});
  }

  getAllConfiguracionpc() : Observable<HttpResponse<Configuracionpc[]>>{
    let url = ClienteApiRestService.BASE_URI + '/configuracion';
    return this.http.get<Configuracionpc[]>(url, {observe: 'response'});
  }

  borrarConfiguracion(id : Number) : Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI + '/configuracion/'+id;
    return this.http.delete<any>(url, {observe: 'response'});
  }

  addConfiguracionpc(conf : Configuracionpc) : Observable<HttpResponse<any>>{
    let url = ClienteApiRestService.BASE_URI + '/configuracion';
    return this.http.post<Configuracionpc>(url, conf, {observe: 'response'})
  }
  /*
  // Ejemplo de get con retorno del cuerpo del response
  getAllVinos() {
    //console.log("dentro de getAllVInos");
    let url = ClienteApiRestService.BASE_URI + '/paraAngular';
    return this.http.get<Vino[]>(url); // Retorna el cuerpo de la respuesta, sin modelo
  }
  // Ejemplo de get con retorno de la respuesta. En el resto de métodos lo hacemos de esta manera
  getAllVinos_ConResponse(): Observable<HttpResponse<Vino[]>> {
    let url = ClienteApiRestService.BASE_URI + '/paraAngular';
    return this.http.get<Vino[]>(url, { observe: 'response' });
  }
  borrarVino(id: String): Observable<HttpResponse<any>> {
    let url = ClienteApiRestService.BASE_URI + '/paraAngular/borrar/' + id;
    return this.http.delete<any>(url, { observe: 'response' });
  }
  anadirVino(vino: Vino): Observable<HttpResponse<any>> {
    let url = ClienteApiRestService.BASE_URI + '/paraAngular';
    return this.http.post<Vino>(url, vino, { observe: 'response' });
  }
  modificarCom(id: String, vino: Vino): Observable<HttpResponse<any>> {
    let url = ClienteApiRestService.BASE_URI + '/paraAngular/' + id;
    return this.http.put<Vino>(url, vino, { observe: 'response' });
  }
  getVino(id: String): Observable<HttpResponse<Vino>> {
    let url = ClienteApiRestService.BASE_URI + '/paraAngular/' + id;
    return this.http.get<Vino>(url, { observe: 'response' });
  }*/
}