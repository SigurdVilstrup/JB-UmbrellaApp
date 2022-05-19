import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpClient } from '@angular/common/http';

import { FactsComponent } from './facts.component';

describe('FactsComponent', () => {
  let component: FactsComponent;
  let fixture: ComponentFixture<FactsComponent>;
  let httpClient: HttpClient = jasmine.createSpyObj('HttpClient', ['get']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FactsComponent ],
      providers: [
        {
          provide: HttpClient,
          useValue: httpClient,
        },
      ],
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FactsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render greeting', () => {
    // Arrange
    const response = { fact: 'A man once ate a bicycle' };
    (httpClient.get as jasmine.Spy).and.returnValue(of(response));

    const fixture = TestBed.createComponent(FactsComponent);

    fixture.detectChanges();
    const compiled = fixture.nativeElement;

    // Assert
    expect(compiled.querySelector('.fact').textContent).toContain(
      response.fact
    );
  });
});
