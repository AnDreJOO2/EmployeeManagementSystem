import {Component, Inject} from '@angular/core';
import {EmployeeService} from "../../services/employee.service";
import {ToastrService} from "ngx-toastr";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {NgForm} from "@angular/forms";
import {Employee} from "../../interfaces/employee";

@Component({
  selector: 'app-delete-employee',
  templateUrl: './delete-employee.component.html',
  styleUrls: ['./delete-employee.component.css']
})
export class DeleteEmployeeComponent {

  constructor(private employeeService: EmployeeService, private toastr: ToastrService, private dialogRef: MatDialogRef<DeleteEmployeeComponent>,
              @Inject(MAT_DIALOG_DATA) public toDelete: Employee) {
  }

  deleteEmployee(form: NgForm) {
    if (form.valid) {
      this.employeeService.deleteEmployee(this.toDelete.id).subscribe({
        next: (value: any) => {
          this.toastr.success('Employee deleted successfully', 'Success', {
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
