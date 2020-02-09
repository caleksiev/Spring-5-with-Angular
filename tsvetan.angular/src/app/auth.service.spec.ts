import {TestBed} from '@angular/core/testing';

import {AuthService} from './auth.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient} from '@angular/common/http';
import {User} from './authentication/register/user.interface';

describe('AuthorizationService', () => {
  let httpMock: HttpClient;
  let httpTestingController: HttpTestingController;
  let authService: AuthService;

  beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [AuthService],
      });
      httpMock = TestBed.get(HttpClient);
      httpTestingController = TestBed.get(HttpTestingController);
      authService = TestBed.get(AuthService);
    }
  );

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(authService).toBeTruthy();
  });

  describe('current user', () => {
    it('current user characteristics should not be set before login', () => {
      expect(authService.getRole()).toBeNull();
      expect(authService.getToken()).toBeNull();
      expect(authService.getUsername()).toBeNull();
      expect(authService.isLoggedIn()).toBeFalsy();
    });

    it('current user characteristics should  be as expected', () => {

      const testUser: User = {
        username: 'pesho',
        password: '1234',
        role: 'DEV'
      };

      const cryptTestUserCharacteristics: string = btoa('pesho:1234');
      authService.sendToken(cryptTestUserCharacteristics);

      const currentUserRequest = httpTestingController.expectOne('http://localhost:8080/users/me');

      expect(currentUserRequest.request.method).toEqual('GET');
      expect(currentUserRequest.request.withCredentials).toBeTruthy();
      expect(currentUserRequest.request.headers.has('Authorization')).toBeTruthy();

      currentUserRequest.flush(testUser, {
        status: 200,
        statusText: 'Current user'
      });

      expect(authService.getRole()).toEqual(testUser.role);
      expect(authService.getToken()).toEqual(cryptTestUserCharacteristics);
      expect(authService.getUsername()).toEqual(testUser.username);
      expect(authService.isLoggedIn()).toBeTruthy();

      authService.logout();
    });
  });
});
