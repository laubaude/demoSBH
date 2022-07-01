package fr.lba.sbh.services;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.lba.sbh.commons.CrudServices;
import fr.lba.sbh.model.Category;

@RestController
@RequestMapping("/api/category")
public class CategoryService extends CrudServices<Category> {

}
