package fr.lba.sbh.commons;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.PersistentObjectException;
import org.hibernate.annotations.NaturalId;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.voodoodyne.jackson.jsog.JSOGGenerator;

@lombok.extern.slf4j.Slf4j
@MappedSuperclass
@JsonIdentityInfo(generator = JSOGGenerator.class)
public abstract class AbstEntity implements Serializable {

    private static final long serialVersionUID = 7901227851616301366L;

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @SequenceGenerator(name = "seq", sequenceName = "seq", initialValue = 1000)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Id
    @lombok.Getter
    @lombok.Setter
    private long idTech;

    // no maximum size and no expiration
    private static Cache<Class<?>, List<PropertyDescriptor>> cache = CacheBuilder.newBuilder().build();

    @JsonIgnore
    public Object invoke(final Object obj, final Method method) {
        try {
            return method.invoke(obj);
        } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
            throw new PersistentObjectException(
                    String.format("La classe %s, method %s. ", getClass().getSimpleName(), method.getName()) + e.getMessage());
        }
    }

    @JsonIgnore
    public List<PropertyDescriptor> getNaturalIdAccessor(final Class<?> classe) {

        List<PropertyDescriptor> result = cache.getIfPresent(classe);
        if (result == null) {
            final List<PropertyDescriptor> descriptors = new ArrayList<PropertyDescriptor>();
            log.info("NaturalIdAccessor not in cache {} ", classe.getSimpleName());
            ReflectionUtils.doWithFields(classe, new ReflectionUtils.FieldCallback() {
                @Override
                public void doWith(Field field) {
                    final PropertyDescriptor descriptor = BeanUtils.getPropertyDescriptor(classe, field.getName());
                    // si le champ à une annotation @NaturalID
                    for (Annotation annotation : field.getDeclaredAnnotations()) {
                        if (annotation.annotationType() == NaturalId.class) {
                            descriptors.add(descriptor);
                            log.debug("Entity : {}. Add field '{}' in the natural ID list.", classe, field.getName());
                            break;
                        }
                    }
                }
            });
            if (descriptors.size() == 0) {
                // no natural Id -> get technical id
                descriptors.add(BeanUtils.getPropertyDescriptor(classe, "idTech"));
            }
            cache.put(classe, descriptors);
            result = descriptors;
        }
        return result;
    }

    @JsonIgnore
    public List<PropertyDescriptor> getNaturalIdAccessor() {
        return getNaturalIdAccessor(this.getClass());
    }

    @Override
    public String toString() {
        StringBuilder value = new StringBuilder();
        try {
            value.append(this.getClass().getSimpleName()).append(' ').append(mapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return value.toString();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() == obj.getClass()) {
            EqualsBuilder equalsBuilder = new EqualsBuilder();
            for (PropertyDescriptor descriptor : getNaturalIdAccessor()) {
                equalsBuilder.append(invoke(this, descriptor.getReadMethod()), invoke(obj, descriptor.getReadMethod()));
            }
            return equalsBuilder.isEquals();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder(17, 37);
        for (PropertyDescriptor descript : getNaturalIdAccessor()) {
            builder.append(invoke(this, descript.getReadMethod()));
        }
        return builder.hashCode();
    }

}
