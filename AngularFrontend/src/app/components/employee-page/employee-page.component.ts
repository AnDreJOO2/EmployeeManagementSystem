import {Component, OnInit} from '@angular/core';
import {EmployeeService} from "../../services/employee.service";
import {EmployeePage} from "../../interfaces/employee-page";
import {MatDialog} from "@angular/material/dialog";
import {AddEmployeeComponent} from "../add-employee/add-employee.component";
import {EditEmployeeComponent} from "../edit-employee/edit-employee.component";
import {DeleteEmployeeComponent} from "../delete-employee/delete-employee.component";
import {Employee} from "../../interfaces/employee";

@Component({
  selector: 'app-employee-page',
  templateUrl: './employee-page.component.html',
  styleUrls: ['./employee-page.component.css']
})
export class EmployeePageComponent implements OnInit {

  // @ts-ignore
  employeePage: EmployeePage

  collectionSize = 0;
  page = 0;
  pageSize = 15;


  constructor(private matDialog: MatDialog, private employeeService: EmployeeService) {
  }

  ngOnInit(): void {
    return this.goToPage(0)
  }

  openNewEmployeeDialog() {
    let dialogRef = this.matDialog.open(AddEmployeeComponent);
    dialogRef.afterClosed().subscribe({
      next: (value) => {
        if (value) {
          this.goToPage(this.page - 1);
        }
      }
    })
  }

  openEditEmployeeDialog(employee: Employee) {
    let dialogRef = this.matDialog.open(EditEmployeeComponent, {data: employee});
    dialogRef.afterClosed().subscribe({
      next: (value) => {
        if (value) {
          this.goToPage(this.page - 1);
        }
      }
    })
  }

  openDeleteEmployeeDialog(employee: Employee) {
    let dialogRef = this.matDialog.open(DeleteEmployeeComponent, {data: employee});
    dialogRef.afterClosed().subscribe({
      next: (value) => {
        if (value) {
          this.goToPage(this.page - 1);
        }
      }
    })
  }

  goToPage(page: number) {
    this.employeeService.getEmployeePage(page, this.pageSize).subscribe(response => {
      this.employeePage = response;
      this.collectionSize = response.totalElements;
    });
  }

  changePageSize(number: number) {
    this.pageSize = number;
    this.page = 0;
  }
}
