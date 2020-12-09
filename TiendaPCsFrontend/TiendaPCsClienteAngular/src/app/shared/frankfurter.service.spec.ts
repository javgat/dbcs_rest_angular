import { TestBed } from '@angular/core/testing';

import { FrankfurterService } from './frankfurter.service';

describe('FrankfurterService', () => {
  let service: FrankfurterService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FrankfurterService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
