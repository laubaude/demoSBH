package fr.lba.sbh.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.lba.sbh.commons.CrudServices;
import fr.lba.sbh.model.Address;

@RestController
@RequestMapping("/api/Address")
public class AddressService extends CrudServices<Address> {

}
