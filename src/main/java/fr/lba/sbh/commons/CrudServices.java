package fr.lba.sbh.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class CrudServices<T extends AbstEntity> {

    @Autowired
    private NaturalRepository<T, Long> repository;

    private String toJson(Object obj) {
        return obj == null ? "null" : obj.toString();
    }

    @JsonRequestMapping(method = RequestMethod.POST)
    public ResponseEntity<T> create(@RequestBody(required = true) T entite)
            throws NotRetryableServerException {
        if (log.isDebugEnabled()) {
            log.debug("create()" + toJson(entite));
        }
        return ResponseEntity.ok(repository.saveAndFlush(entite));
    }

    @JsonRequestMapping(method = RequestMethod.POST, value = "/get")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<T> getNatural(@RequestBody(required = true) T entity)
            throws NotRetryableServerException {
        Optional<T> result = null;
        if (log.isDebugEnabled()) {
            log.debug("getNatural()" + toJson(entity));
        }
        long idTech = entity.getIdTech();
        if (idTech != 0l) {
            result = repository.findById(idTech);
        } else {
            result = repository.findByNaturalId(entity);
        }
        return ResponseEntity.of(result);
    }

    @JsonRequestMapping(method = RequestMethod.GET, value = "/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<T> getById(@PathVariable("id") long id)
            throws NotRetryableServerException {
        if (log.isDebugEnabled()) {
            log.debug("getById({}) ", id);
        }
        return ResponseEntity.of(Optional.ofNullable(repository.findById(id).get()));
    }

    @JsonRequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<T>> getAll() {
        if (log.isDebugEnabled()) {
            log.debug("getAll() ");
        }
        List<T> result = new ArrayList<>();
        repository.findAll().forEach(obj -> result.add(obj));
        return ResponseEntity.ok(result);
    }

    @JsonRequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> delete(@PathVariable("id") long id) throws NotRetryableServerException {
        if (log.isDebugEnabled()) {
            log.debug("delete(id) " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @JsonRequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody(required = true) T value) throws NotRetryableServerException {
        if (log.isDebugEnabled()) {
            log.debug("update()" + toJson(value));
        }
        try {
            T entite = repository.save(value);
            return ResponseEntity.ok(entite);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }
}
