import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {EmployeePage} from "../interfaces/employee-page";
import {NewEmployee} from "../classes/new-employee";
import {Observable} from "rxjs";
import {EmployeeSearchCriteria} from "../classes/employee-search-criteria";

@Injectable({
  providedIn: 'root'
})
export class EmployeeService {

  constructor(private httpClient: HttpClient) {
  }

  private employeeUrl = "http://localhost:8080/api/employees";

  getEmployeePage(employeeSearchCriteria: EmployeeSearchCriteria): Observable<any> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("number", employeeSearchCriteria.number);
    queryParams = queryParams.append("size", employeeSearchCriteria.size);
    queryParams = queryParams.append("sortBy", employeeSearchCriteria.sortBy);
    queryParams = queryParams.append("direction", employeeSearchCriteria.direction);

    if (employeeSearchCriteria.getFirstNameOrLastNameOrEmailLike != null) {
      queryParams = queryParams.append("firstNameOrLastNameOrEmailLike", employeeSearchCriteria.getFirstNameOrLastNameOrEmailLike);
    }

    if (employeeSearchCriteria.salaryGreaterEqual != null) {
      queryParams = queryParams.append("salaryGreaterEqual", employeeSearchCriteria.salaryGreaterEqual);
    }

    if (employeeSearchCriteria.salaryLessEqual != null) {
      queryParams = queryParams.append("salaryLessEqual", employeeSearchCriteria.salaryLessEqual);
    }

    return this.httpClient.get<EmployeePage>(this.employeeUrl, {params: queryParams});
  }

  addEmployee(newEmployee: NewEmployee): Observable<any> {
    return this.httpClient.post(this.employeeUrl, newEmployee);
  }

  editEmployee(id: number, newEmployee: NewEmployee): Observable<any> {
    return this.httpClient.put(this.employeeUrl + '/' + id, newEmployee);
  }

  deleteEmployee(id: number): Observable<any> {
    return this.httpClient.delete(this.employeeUrl + '/' + id);
  }

  exportEmployees(type: string): Observable<Blob> {
    let queryParams = new HttpParams();
    queryParams = queryParams.append("type", type);
    return this.httpClient.get(this.employeeUrl + '/export', {params: queryParams, responseType: 'blob'});
  }

  importEmployees(file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file);
    return this.httpClient.post(this.employeeUrl + '/import', formData, {reportProgress: true});
  }
}
