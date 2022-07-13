package fr.lba.sbh;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import fr.lba.sbh.commons.AbstEntity;
import fr.lba.sbh.model.Address;
import fr.lba.sbh.model.Customer;
import fr.lba.sbh.model.Order;
import fr.lba.sbh.model.Pet;
import fr.lba.sbh.model.Status;
import fr.lba.sbh.services.PetService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
class DemoSbhApplicationTests {

	private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

	@Autowired
	private PetService petService;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private String getUrl(String api) {
		return "http://localhost:" + port + "/api/" + api;
	}

	@SuppressWarnings("unchecked")
	private <T extends AbstEntity> T postForEntity(String api, T entity) {
		return (T) restTemplate.postForEntity(getUrl(api), entity, entity.getClass()).getBody();
	}

	private <T> T getForEntity(String api, Class<T> clazz) {
		return (T) restTemplate.getForEntity(getUrl(api), clazz).getBody();
	}

	@SuppressWarnings({ "unchecked" })
	private <T> T getFromNaturals(String api, T entity) {
		return (T) restTemplate.postForEntity(getUrl(api + "/get"), entity, entity.getClass()).getBody();
	}

	private <T> T getFromId(String api, long id, Class<T> clazz) {
		return (T) restTemplate.getForEntity(getUrl(api + "/get/" + id), clazz).getBody();
	}
	
	private void print(Object obj) {
		try {
			System.out.println(obj.getClass().getSimpleName());
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
		} catch (JsonProcessingException e) {
			System.out.println(e);
		}
	}


	@Test
	@org.junit.jupiter.api.Order(1)
	void contextLoads() {
		assertNotNull(petService);
	}

	@Test
	@org.junit.jupiter.api.Order(2)
	void pet() {
		// getAll
		Pet[] pets = getForEntity("pet", Pet[].class);
		assertNotNull(pets);
		assertTrue(pets.length > 0);
		print(pets);

		// getById
		Pet pet = getFromId("pet", pets[0].getIdTech(), Pet.class);
		assertNotNull(pet);
		print(pet);

		// getByNaturalId
		pet.setIdTech(0);
		Pet pet2 = getFromNaturals("pet", new Pet("Rex", null, null));
		assertNotNull(pet2);
		print(pet2);

		// getByNaturalId
		pet2 = getFromNaturals("pet", new Pet("Rex", null, null));
		assertNotNull(pet2);

		assertTrue(pet.equals(pet2));

		assertTrue(Integer.compare(pet.hashCode(), pet2.hashCode()) == 0);

	}

	@Test
	@org.junit.jupiter.api.Order(3)
	void customer() {
		Address add = postForEntity("address", new Address("Beauregard", "Paris", "RP", "75001"));
		assertTrue("Beauregard".equals(add.getStreet()));
		Customer cust = postForEntity("customer", new Customer("Foo", add));
		assertTrue("Foo".equals(cust.getUserName()));
		print(cust);
	}

	@Test
	@org.junit.jupiter.api.Order(4)
	void order() {
		Pet pet = getFromNaturals("pet", new Pet("Rex", null, null));
		Customer cust = getFromNaturals("customer", new Customer("Foo", null));
		Order order = postForEntity("order", new Order(pet,"idorder",cust,LocalDate.now(),Status.inProgress,false));
		assertNotNull(order.getCustomer());
		assertNotNull(order.getPet());
		print(order);
	}

	
}
