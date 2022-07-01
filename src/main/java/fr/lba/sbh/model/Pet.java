package fr.lba.sbh.model;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import fr.lba.sbh.commons.AbstEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor()
@EqualsAndHashCode(of = {}, callSuper = true)
@Entity
@NaturalIdCache
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Pet extends AbstEntity {

    private static final long serialVersionUID = -5791061784866523558L;

    @NaturalId
    private String name;

    @ManyToOne
    private Category category;

    private Status status;

}
