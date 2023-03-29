import {Component, OnInit} from '@angular/core';
import {EmployeeService} from "../../services/employee.service";
import {EmployeePage} from "../../interfaces/employee-page";
import {MatDialog} from "@angular/material/dialog";
import {AddEmployeeComponent} from "../add-employee/add-employee.component";
import {EditEmployeeComponent} from "../edit-employee/edit-employee.component";
import {DeleteEmployeeComponent} from "../delete-employee/delete-employee.component";
import {Employee} from "../../interfaces/employee";
import {ToastrService} from "ngx-toastr";
import {EmployeeSearchCriteria} from "../../classes/employee-search-criteria";

@Component({
  selector: 'app-employee-page',
  templateUrl: './employee-page.component.html',
  styleUrls: ['./employee-page.component.css']
})
export class EmployeePageComponent implements OnInit {

  // @ts-ignore
  employeePage: EmployeePage

  employeeSearchCriteria: EmployeeSearchCriteria = new EmployeeSearchCriteria();

  selectedFiles?: FileList;
  currentFile?: File;


  fileName = 'Select File';

  constructor(private matDialog: MatDialog, private employeeService: EmployeeService, private toastr: ToastrService) {
  }

  ngOnInit(): void {
    return this.goToPage(0)
  }

  openNewEmployeeDialog() {
    let dialogRef = this.matDialog.open(AddEmployeeComponent);
    dialogRef.afterClosed().subscribe({
      next: (value) => {
        if (value) {
          this.goToPage(this.employeeSearchCriteria.number - 1);
        }
      }
    })
  }

  openEditEmployeeDialog(employee: Employee) {
    let dialogRef = this.matDialog.open(EditEmployeeComponent, {data: employee});
    dialogRef.afterClosed().subscribe({
      next: (value) => {
        if (value) {
          this.goToPage(this.employeeSearchCriteria.number - 1);
        }
      }
    })
  }

  openDeleteEmployeeDialog(employee: Employee) {
    let dialogRef = this.matDialog.open(DeleteEmployeeComponent, {data: employee});
    dialogRef.afterClosed().subscribe({
      next: (value) => {
        if (value) {
          this.goToPage(this.employeeSearchCriteria.number - 1);
        }
      }
    })
  }

  goToPage(pageNumber: number) {
    this.employeeSearchCriteria.number = pageNumber
    this.search(this.employeeSearchCriteria);
  }

  search(employeeSearchCriteria: EmployeeSearchCriteria) {
    this.employeeService.getEmployeePage(employeeSearchCriteria).subscribe(response => {
      this.employeePage = response;
    });
  }

  changePageSize(size: number) {
    this.employeeSearchCriteria.size = size;
    this.goToPage(0);
  }

  export(type: string) {
    this.employeeService.exportEmployees(type).subscribe(
      response => {

        const a = document.createElement('a')
        const objectUrl = URL.createObjectURL(response)
        a.href = objectUrl
        a.download = 'employees.' + type;
        a.click();
        URL.revokeObjectURL(objectUrl);
        this.toastr.success('File exported successfully', 'Success', {
          timeOut: 2000,
        });
      },
      error => {
        this.toastr.error(error.error, 'Error', {
          timeOut: 2000,
        });
      });
  }

  import() {
    if (this.selectedFiles) {
      const file: File | null = this.selectedFiles.item(0);

      if (file) {
        this.currentFile = file;

        this.employeeService.importEmployees(this.currentFile).subscribe({
          next: (event: any) => {
            this.toastr.success('File imported successfully', 'Success', {
              timeOut: 2000,
            });
            this.goToPage(0);
          },
          error: (err: any) => {
            this.toastr.error(err.error, 'Error', {
              timeOut: 2000,
            });

            this.currentFile = undefined;
          }
        });
      }
      this.selectedFiles = undefined;
    }
  }

  selectFile(event: any) {
    this.selectedFiles = event.target.files;
    this.import();
  }

  updateFirstNameLike($event: string) {
    this.employeeSearchCriteria.firstNameLike = $event
    this.employeeSearchCriteria.number = 0;
  }

  updateLastNameLike($event: string) {
    this.employeeSearchCriteria.lastNameLike = $event
  }

  updateEmailNameLike($event: string) {
    this.employeeSearchCriteria.emailLike = $event
  }

  updateSalaryGreaterEqual($event: number) {
    this.employeeSearchCriteria.salaryGreaterEqual = $event
  }

  updateSalaryLessEqual($event: number) {
    this.employeeSearchCriteria.salaryLessEqual = $event
  }

  updateSortingBy($event: string) {
    this.employeeSearchCriteria.sortBy = $event
  }

  updateOrdering($event: string) {
    this.employeeSearchCriteria.direction = $event
  }
}
