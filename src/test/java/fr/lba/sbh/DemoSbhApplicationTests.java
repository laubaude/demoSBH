package fr.lba.sbh;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import fr.lba.sbh.model.Pet;
import fr.lba.sbh.services.PetService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class DemoSbhApplicationTests {

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private PetService petService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        assertNotNull(petService);
    }

    @Test
    void pet() {
        ResponseEntity<Pet[]> pets = this.restTemplate.getForEntity("http://localhost:" + port + "/api/pet", Pet[].class);
        print(pets.getBody());
        Pet pet = this.restTemplate.getForEntity("http://localhost:" + port + "/api/pet/get/" + pets.getBody()[0].getIdTech(), Pet.class).getBody();
        print(pet);
        pet.setIdTech(0);

        Pet pet2 = this.restTemplate.postForEntity("http://localhost:" + port + "/api/pet/get/", new Pet("Rex", null, null), Pet.class).getBody();
        print(pet2);
        this.restTemplate.postForEntity("http://localhost:" + port + "/api/pet/get/", new Pet("Rex", null, null), Pet.class).getBody();
    }

    private void print(Object obj) {
        try {
            System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
    }

}
