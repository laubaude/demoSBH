package fr.lba.sbh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import fr.lba.sbh.jpa.CategoryRepository;
import fr.lba.sbh.model.Category;
import fr.lba.sbh.model.Pet;
import fr.lba.sbh.model.Status;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    CategoryRepository catRepo;

    @Autowired
    CrudRepository<Pet, Long> petRepo;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Category dog = new Category("Dog");
        catRepo.save(dog);
        catRepo.save(new Category("Cat"));
        catRepo.save(new Category("Bird"));

        petRepo.save(new Pet("Rex", dog, Status.pending));
    }

}
