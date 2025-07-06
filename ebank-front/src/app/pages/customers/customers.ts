import { Component, OnInit } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CustomerService} from '../../services/customer-service';
import {Observable} from 'rxjs';
import {AsyncPipe} from '@angular/common';
import {Customer} from '../../models/Customer.model';

@Component({
  selector: 'app-customers',
  imports: [
    AsyncPipe
  ],
  templateUrl: './customers.html',
  styleUrl: './customers.css'
})
export class Customers implements OnInit {
  customers$ !: Observable<Array<Customer>>;  // initialize as empty array
  constructor(private customerService: CustomerService) {

  }

  ngOnInit() {
    this.customers$ = this.customerService.getCustomers();
  }
}
