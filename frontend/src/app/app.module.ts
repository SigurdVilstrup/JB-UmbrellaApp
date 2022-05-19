import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppComponent } from './app.component';
import { FactsComponent } from './facts/facts.component';
import { AddressRainComponent } from './address-rain/address-rain.component';

@NgModule({
  declarations: [
    AppComponent,
    FactsComponent,
    AddressRainComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
  ],
  exports: [
    FactsComponent,
    AddressRainComponent,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
