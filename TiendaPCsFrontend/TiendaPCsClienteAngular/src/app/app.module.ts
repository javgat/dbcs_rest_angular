import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';

import { ClienteApiRestService } from './shared/cliente-api-rest.service';
import { DataService } from './shared/data.service';
import { CatalogoComponent } from './catalogo/catalogo.component';
import { NuevaConfiguracionComponent } from './nueva-configuracion/nueva-configuracion.component';
import { ModificarConfiguracionComponent } from './modificar-configuracion/modificar-configuracion.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    CatalogoComponent,
    NuevaConfiguracionComponent,
    ModificarConfiguracionComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule 
  ],
  providers: [
    ClienteApiRestService,
    DataService
    ],
  bootstrap: [AppComponent]
})
export class AppModule { }
