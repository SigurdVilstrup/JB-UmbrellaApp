import { HttpClient } from '@angular/common/http';
import { Component, NgModule, OnInit } from '@angular/core';
import { Observable } from 'rxjs';

interface queryResponse {
  fact: string;
}

@Component({
  selector: 'app-facts',
  templateUrl: './facts.component.html',
  styleUrls: ['./facts.component.scss']
})
export class FactsComponent implements OnInit {
  response$: Observable<queryResponse>;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.getFact();
  }

  private getFact() {
    this.response$ = this.http.get<queryResponse>('/api/fact');
  }
}
