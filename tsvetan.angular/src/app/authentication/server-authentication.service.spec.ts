import {TestBed} from '@angular/core/testing';

import {ServerAuthenticationService} from './server-authentication.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {HttpClient} from '@angular/common/http';
import {User} from './register/user.interface';


describe('ServerAuthenticationService', () => {
  let httpMock: HttpClient;
  let httpTestingController: HttpTestingController;
  let serverAuthenticationService: ServerAuthenticationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ServerAuthenticationService]
    });

    httpMock = TestBed.get(HttpClient);
    httpTestingController = TestBed.get(HttpTestingController);
    serverAuthenticationService = TestBed.get(ServerAuthenticationService);

  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it('should be created', () => {
    expect(serverAuthenticationService).toBeTruthy();
  });

  describe('register', () => {
    it('http should register user successfully with status 200 ', () => {

      const testUser: User = {
        username: 'ivan',
        password: '1234'
      };
      serverAuthenticationService.register(testUser).subscribe((registeredUser) => {
        expect(registeredUser.status === 200);
        expect(registeredUser).toEqual(testUser);
      }, () => {
        fail('should have succeed with status 200');
      });

      const registerRequest = httpTestingController.expectOne('http://localhost:8080/register');
      expect(registerRequest.request.method).toEqual('POST');

      registerRequest.flush(testUser, {
        status: 201,
        statusText: 'Registered'
      });
    });

    it('http should not register user and return status 409', () => {
      const testUser: User = {
        username: 'pesho',
        password: '1234'
      };

      const CONFLICT_MESSAGE = 'The user already exists.';
      serverAuthenticationService.register(testUser).subscribe(() => {
        fail('should have failed with error 409');
      }, (error) => {
        expect(error.status).toEqual(409);
        expect(error.error).toEqual(CONFLICT_MESSAGE);
      });

      const registerRequest = httpTestingController.expectOne('http://localhost:8080/register');
      expect(registerRequest.request.method).toEqual('POST');

      registerRequest.flush(CONFLICT_MESSAGE, {
        status: 409,
        statusText: CONFLICT_MESSAGE
      });
    });
  });

  describe('login', () => {

    it('success login', () => {
      const testUsername = 'bob';
      const testPassword = '1234';

      const OK_MESSAGE = 'The user is logged in';

      serverAuthenticationService.login(testUsername, testPassword).subscribe((successLoginMessage) => {
        expect(successLoginMessage).toEqual(OK_MESSAGE);
      }, error => {
        fail('should succeed with status 200');
      });

      const loginRequest = httpTestingController.expectOne('http://localhost:8080/login');

      expect(loginRequest.request.method).toEqual('POST');
      expect(loginRequest.request.headers.has('Authorization')).toEqual(true);

      loginRequest.flush(OK_MESSAGE, {
        status: 200,
        statusText: 'Logged'
      });
    });

    it('unsuccessful login', () => {
      const testUsername = 'bob';
      const testPassword = '1234';

      const UNAUTHORIZED_MESSAGE = 'The credentials are not valid.';

      serverAuthenticationService.login(testUsername, testPassword).subscribe(() => {
        fail('should have failed with status 403');

      }, error => {
        expect(error.status).toEqual(403);
        expect(error.error).toEqual(UNAUTHORIZED_MESSAGE);
      });

      const loginRequest = httpTestingController.expectOne('http://localhost:8080/login');

      expect(loginRequest.request.method).toEqual('POST');
      expect(loginRequest.request.headers.has('Authorization')).toEqual(true);

      loginRequest.flush(UNAUTHORIZED_MESSAGE, {
        status: 403,
        statusText: 'UNAUTHORIZED'
      });
    });
  });
});
