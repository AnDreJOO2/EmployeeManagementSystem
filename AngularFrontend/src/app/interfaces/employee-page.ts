import {Employee} from "./employee";
import {Sort} from "./sort";
import {Pageable} from "./pageable";

export interface EmployeePage {
  content: Employee[];
  pageable: Pageable;
  last: boolean;
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  sort: Sort;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}
