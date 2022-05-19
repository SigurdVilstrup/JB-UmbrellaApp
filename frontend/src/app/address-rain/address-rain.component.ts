import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Component, OnInit } from '@angular/core';
import { NgForm  } from '@angular/forms';

interface WeatherRainQueryResponse {
  closest_address: string;
  probability_of_precipitation: string;
}

@Component({
  selector: 'app-address-rain',
  templateUrl: './address-rain.component.html',
  styleUrls: ['./address-rain.component.scss']
})
export class AddressRainComponent implements OnInit {
  weatherResponse$: Observable<WeatherRainQueryResponse>;
  showResult = false;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {}

  onSubmit(value:NgForm): void{
    this.showResult = true;
    this.getClosestAddressAndPerspirationChance(value.value.address_query);
  }
 
  private getClosestAddressAndPerspirationChance(address_query:string){
    this.weatherResponse$ = this.http.get<WeatherRainQueryResponse>('/api/address/'+address_query);
  }
}
