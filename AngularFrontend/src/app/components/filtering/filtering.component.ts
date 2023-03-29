import {Component, EventEmitter, Output} from '@angular/core';
import {NgForm} from "@angular/forms";

interface OrderingInterface {
  value: string;
  viewValue: string;
}

@Component({
  selector: 'app-filtering',
  templateUrl: './filtering.component.html',
  styleUrls: ['./filtering.component.css']
})
export class FilteringComponent {

  @Output() firstNameLike = new EventEmitter<string>();
  @Output() lastNameLike = new EventEmitter<string>();
  @Output() emailLike = new EventEmitter<string>();
  @Output() salaryGreaterEqual = new EventEmitter<number>();
  @Output() salaryLessEqual = new EventEmitter<number>();

  @Output() sortBy = new EventEmitter<string>();
  @Output() ordering = new EventEmitter<string>();


  selectedSearch: string = '';
  selectedMin: number | string = '';
  selectedMax: number | string = '';
  selectedOrdering: string = 'ASC';
  selectedSortingBy: string = 'id';

  orderingOptions = [
    {name: 'Ascending', value: 'ASC'},
    {name: 'Descending', value: 'DESC'}
  ]
  sortingOptions = [
    {name: 'Id', value: 'id'},
    {name: 'First Name', value: 'firstName'},
    {name: 'Last Name', value: 'lastName'},
    {name: 'Email', value: 'email'},
    {name: 'Salary', value: 'salary'},
  ]

  setFirstNameLike(value: string | undefined) {
    this.firstNameLike.emit(value);
  }

  setLastNameLike(value: string | undefined) {
    this.lastNameLike.emit(value);
  }

  setEmailLike(value: string | undefined) {
    this.emailLike.emit(value);
  }

  setSalaryGreaterEqual(value: number | undefined) {
    this.salaryGreaterEqual.emit(value);
  }

  setSalaryLessEqual(value: number | undefined) {
    this.salaryLessEqual.emit(value);
  }

  setSortBy(value: string) {
    this.sortBy.emit(value);
  }

  setOrdering(value: string) {
    this.ordering.emit(value);
  }

  submit(form: NgForm) {
    let firstNameLastNameEmailLike = form.form.value.firstNameLastNameEmail;
    let salaryGreaterEqual = form.form.value.salaryGreaterEqual;
    let salaryLessEqual = form.form.value.salaryLessEqual;

    let sortBy = form.form.value.sortBy;
    let ordering = form.form.value.ordering;

    this.setFirstNameLike(firstNameLastNameEmailLike);
    this.setLastNameLike(firstNameLastNameEmailLike);
    this.setEmailLike(firstNameLastNameEmailLike);
    this.setSalaryGreaterEqual(salaryGreaterEqual);
    this.setSalaryLessEqual(salaryLessEqual);

    this.setSortBy(sortBy);
    this.setOrdering(ordering);
  }

  setDefault() {
    this.selectedSearch = '';
    this.selectedMin = '';
    this.selectedMax = '';
    this.selectedSortingBy = 'id';
    this.selectedOrdering = 'ASC';
  }
}
