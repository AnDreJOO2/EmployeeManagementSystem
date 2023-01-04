import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {EmployeePage} from "../interfaces/employee-page";

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private httpClient: HttpClient) {
  }

  private employeeUrl = "http://localhost:8080/api/employees";

  getEmployeePage() {
    return this.httpClient.get<EmployeePage>(this.employeeUrl);
  }
}
