import {Component, Inject} from '@angular/core';
import {EmployeeService} from "../../services/employee.service";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {NgForm} from "@angular/forms";
import {NewEmployee} from "../../classes/new-employee";
import {Employee} from "../../interfaces/employee";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-edit-employee',
  templateUrl: './edit-employee.component.html',
  styleUrls: ['./edit-employee.component.css']
})

export class EditEmployeeComponent {

  constructor(private employeeService: EmployeeService, private toastr: ToastrService, private dialogRef: MatDialogRef<EditEmployeeComponent>,
              @Inject(MAT_DIALOG_DATA) public toEdit: Employee) {
  }

  editEmployee(employeeId: number, form: NgForm) {
    let firstname = form.form.value.firstname;
    let lastname = form.form.value.lastname;
    let email = form.form.value.email;
    let salary = form.form.value.salary;

    if (form.valid &&
      this.toEdit.firstName != firstname ||
      this.toEdit.lastName != lastname ||
      this.toEdit.email != email ||
      this.toEdit.salary != salary
    ) {

      let editedEmployee: NewEmployee = {
        firstName: firstname,
        lastName: lastname,
        email: email,
        salary: salary,
      }

      this.employeeService.editEmployee(employeeId, editedEmployee).subscribe({
        next: (value: any) => {
          this.toastr.success('Employee edited successfully', 'Success', {
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
    } else {
      this.toastr.error('Employee data not changed', 'Error', {
        timeOut: 2000,
      })
    }

  }
}
