package fr.lba.sbh.commons;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.NaturalIdLoadAccess;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
public class NaturalRepositoryImpl<T extends AbstEntity, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements NaturalRepository<T, ID> {

    @Autowired
    private ApplicationContext context;

    private final EntityManager entityManager;

    public NaturalRepositoryImpl(JpaEntityInformation<T, ID> entityInformation,
            EntityManager entityManager) {
        super(entityInformation, entityManager);

        this.entityManager = entityManager;
    }

    @Override
    public Optional<T> findBySimpleNaturalId(ID naturalId) {

        Optional<T> entity = entityManager.unwrap(Session.class)
                .bySimpleNaturalId(this.getDomainClass())
                .loadOptional(naturalId);

        return entity;
    }

    @Override
    public Optional<T> findByNaturalId(Map<String, Object> naturalIds) {

        NaturalIdLoadAccess<T> loadAccess = entityManager.unwrap(Session.class).byNaturalId(this.getDomainClass());
        naturalIds.forEach(loadAccess::using);

        return loadAccess.loadOptional();
    }

    @Override
    public Optional<T> findByNaturalId(T entity) {
        NaturalIdLoadAccess<T> loadAccess = entityManager.unwrap(Session.class).byNaturalId(this.getDomainClass());

        for (PropertyDescriptor descriptor : entity.getNaturalIdAccessor(entity.getClass())) {
            Object object = entity.invoke(entity, descriptor.getReadMethod());
            if (object == null) {
                log.debug("La recherche getWithNaturalId n'a pas abouti car le champ {} de l'objet {} est null", descriptor.getName(), entity);
                return null;
            }
            if (object instanceof AbstEntity) {
                object = context.getBean(object.getClass().getSimpleName() + "Repository");
            }
            loadAccess.using(descriptor.getName(), object);
        }

        Optional<T> load = loadAccess.loadOptional();
        log.debug("Trouvé dans le cache par id fonctionnel : {}", load.isPresent());
        return load;
    }

}