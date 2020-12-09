import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CatalogoComponent } from './catalogo/catalogo.component';

import { LoginComponent } from './login/login.component'
import { ModificarConfiguracionComponent } from './modificar-configuracion/modificar-configuracion.component';
import { NuevaConfiguracionComponent } from './nueva-configuracion/nueva-configuracion.component';

const routes: Routes = [
  {path: 'login', component:LoginComponent},
  {path: 'catalogo', component:CatalogoComponent},
  {path: 'catalogo/nuevo', component:NuevaConfiguracionComponent},
  {path: 'catalogo/:id/modificar', component:ModificarConfiguracionComponent},
  {path: '**', redirectTo:'login', pathMatch:'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
