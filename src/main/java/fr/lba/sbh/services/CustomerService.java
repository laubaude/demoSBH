package fr.lba.sbh.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.lba.sbh.commons.CrudServices;
import fr.lba.sbh.model.Customer;

@RestController
@RequestMapping("/api/customer")
public class CustomerService extends CrudServices<Customer> {

}
