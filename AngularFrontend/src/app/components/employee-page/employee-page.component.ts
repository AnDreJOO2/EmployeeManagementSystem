import {Component, OnInit} from '@angular/core';
import {EmployeeService} from "../../services/employee.service";
import {EmployeePage} from "../../interfaces/employee-page";

@Component({
  selector: 'app-employee-page',
  templateUrl: './employee-page.component.html',
  styleUrls: ['./employee-page.component.css']
})
export class EmployeePageComponent implements OnInit{

  employeePage: EmployeePage | undefined

  constructor(private employeeService: EmployeeService) {
  }

  ngOnInit(): void {
    this.employeeService.getEmployeePage().subscribe(response =>{
      this.employeePage = response;
      console.log(this.employeePage);
    });
  }
}
