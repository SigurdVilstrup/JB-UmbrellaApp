import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { FormsModule, NgForm } from '@angular/forms';

import { AddressRainComponent } from './address-rain.component';
import { By } from '@angular/platform-browser';


describe('AddressRainComponent', () => {
  let component: AddressRainComponent;
  let fixture: ComponentFixture<AddressRainComponent>;
  let httpClient: HttpClient = jasmine.createSpyObj('HttpClient', ['get']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddressRainComponent ],
      providers: [
        {
          provide: HttpClient,
          useValue: httpClient,
        },
      ],
      imports: [FormsModule],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AddressRainComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Should change visibility of results on submit', () => {
    // Arrange
    const testForm = <NgForm>{
      value: {
        address_query: "Vestergade 8",
      }
    };
    const fixture = TestBed.createComponent(AddressRainComponent);

    expect(component.showResult).withContext("Initially not visible").toBe(false);

    component.onSubmit(testForm);

    fixture.detectChanges();
    const compiled = fixture.nativeElement;

    expect(component.showResult).withContext("Now visible").toBe(true);
  });

  it('Should render address', () => {
    const weatherResponse = { 
      closest_address: 'Vestergade 8, 8600 Silkeborg',
      probability_of_precipitation: '10.11'
    };
    const testForm = <NgForm>{
      value: {
        address_query: "Vestergade 8",
      }
    };
    (httpClient.get as jasmine.Spy).and.returnValue(of(weatherResponse));
    const fixture = TestBed.createComponent(AddressRainComponent);
    
    fixture.debugElement.query(By.css('form')).triggerEventHandler('ngSubmit', null);

    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    
    expect(compiled.querySelector('.closest_address').textContent).toContain(
      weatherResponse.closest_address
    );
    expect(compiled.querySelector('.probability_of_precipitation').textContent).toContain(
      weatherResponse.probability_of_precipitation
    );
  });

  it('Should render precipitation', () => {
    const weatherResponse = { 
      closest_address: 'Vestergade 8, 8600 Silkeborg',
      probability_of_precipitation: '10.11'
    };
    const testForm = <NgForm>{
      value: {
        address_query: "Vestergade 8",
      }
    };
    (httpClient.get as jasmine.Spy).and.returnValue(of(weatherResponse));
    const fixture = TestBed.createComponent(AddressRainComponent);
    
    fixture.debugElement.query(By.css('form')).triggerEventHandler('ngSubmit', null);

    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    
    expect(compiled.querySelector('.probability_of_precipitation').textContent).toContain(
      weatherResponse.probability_of_precipitation
    );
  });

});