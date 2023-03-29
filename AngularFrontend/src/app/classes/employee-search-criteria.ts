export class EmployeeSearchCriteria {

  number: number = 0;
  size: number = 15;
  sortBy: string = "id";
  direction: string = "ASC";
  firstNameLike?: string;
  lastNameLike?: string;
  emailLike?: string;
  salaryGreaterEqual?: number;
  salaryLessEqual?: number;
}
