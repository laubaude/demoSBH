package fr.lba.sbh.jpa;

import java.util.List;

import fr.lba.sbh.commons.NaturalRepository;
import fr.lba.sbh.model.Customer;

public interface CustomerRepository extends NaturalRepository<Customer, Long> {

    List<Customer> findByUserName(String userName);

    Customer findByIdTech(long id);
}