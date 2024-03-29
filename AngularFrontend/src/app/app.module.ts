import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from "@angular/common/http";

import { AppComponent } from './app.component';
import { EmployeePageComponent } from './components/employee-page/employee-page.component';
import { AddEmployeeComponent } from './components/add-employee/add-employee.component';
import { EditEmployeeComponent } from './components/edit-employee/edit-employee.component';
import { DeleteEmployeeComponent } from './components/delete-employee/delete-employee.component';
import { MatFormFieldModule} from "@angular/material/form-field";
import { MatInputModule} from "@angular/material/input";
import { FormsModule } from "@angular/forms";
import { MatDialogModule } from "@angular/material/dialog";
import { NgxPaginationModule } from "ngx-pagination";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import {MatButtonModule} from "@angular/material/button";
import {ToastrModule} from "ngx-toastr";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import { FilteringComponent } from './components/filtering/filtering.component';
import {MatSelectModule} from "@angular/material/select";
import {MatRadioModule} from "@angular/material/radio";

@NgModule({
  declarations: [
    AppComponent,
    EmployeePageComponent,
    AddEmployeeComponent,
    EditEmployeeComponent,
    DeleteEmployeeComponent,
    FilteringComponent
  ],
    imports: [
        BrowserModule,
        HttpClientModule,
        MatFormFieldModule,
        MatInputModule,
        FormsModule,
        MatDialogModule,
        NgxPaginationModule,
        NgbModule,
        MatButtonModule,
        BrowserAnimationsModule,
        ToastrModule.forRoot(),
        MatSelectModule,
        MatRadioModule,
    ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
