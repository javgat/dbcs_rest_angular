import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({ providedIn: 'root' })

export class FrankfurterService {
  
  private static readonly BASE_URI = 'https://api.frankfurter.app';
  constructor(private http: HttpClient) { }

  getFactor(from: String) : Observable<HttpResponse<any>>{
    let url = FrankfurterService.BASE_URI + '/2020-01-01?from='+from+'&to=EUR';
    console.log("url es: "+url);
    return this.http.get<any>(url, {observe: 'response'});
  }
}
