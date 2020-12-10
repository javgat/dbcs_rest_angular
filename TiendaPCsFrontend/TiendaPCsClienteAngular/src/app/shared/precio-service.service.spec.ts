import { TestBed } from '@angular/core/testing';

import { PrecioServiceService } from './precio-service.service';

describe('PrecioServiceService', () => {
  let service: PrecioServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PrecioServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
