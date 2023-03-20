import {Component} from '@angular/core';
import {NgForm} from "@angular/forms";
import {EmployeeService} from "../../services/employee.service";
import {NewEmployee} from "../../classes/new-employee";
import {MatDialogRef} from "@angular/material/dialog";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-add-employee',
  templateUrl: './add-employee.component.html',
  styleUrls: ['./add-employee.component.css']
})
export class AddEmployeeComponent {

  constructor(private employeeService: EmployeeService, private toastr: ToastrService, private dialogRef: MatDialogRef<AddEmployeeComponent>) {
  }


  addEmployee(form: NgForm) {
    if (form.valid) {
      let firstname = form.form.value.firstname;
      let lastname = form.form.value.lastname;
      let email = form.form.value.email;
      let salary = form.form.value.salary;
      let newEmployee: NewEmployee = {
        firstName: firstname,
        lastName: lastname,
        email: email,
        salary: salary,
      }

      this.employeeService.addEmployee(newEmployee).subscribe({
        next: (value: any) => {
          this.toastr.success('Employee added successfully', 'Success', {
            timeOut: 2000,
          });
          this.dialogRef.close(true);
        },
        error: (error: any) => {
          this.toastr.error('Error: ' + error.message, 'Error', {
            timeOut: 5000,
          })
        }
      })
    }

  }
}
