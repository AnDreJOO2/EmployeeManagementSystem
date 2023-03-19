import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {EmployeePage} from "../interfaces/employee-page";

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private httpClient: HttpClient) {
  }

  private employeeUrl = "http://localhost:8080/api/employees";

  getEmployeePage(page: number, pageSize: number) {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("page", page);
    queryParams = queryParams.append("pageSize", pageSize);

    return this.httpClient.get<EmployeePage>(this.employeeUrl, {params: queryParams});
  }
}
