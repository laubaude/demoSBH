package fr.lba.sbh.commons;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface NaturalRepository<T extends AbstEntity, ID extends Serializable> extends JpaRepository<T, ID> {

    Optional<T> findBySimpleNaturalId(ID naturalId);

    Optional<T> findByNaturalId(Map<String, Object> naturalIds);

    Optional<T> findByNaturalId(T entity);
}