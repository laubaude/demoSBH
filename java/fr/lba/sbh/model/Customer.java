package fr.lba.sbh.model;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.hibernate.annotations.NaturalId;

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
public class Customer extends AbstEntity {

    private static final long serialVersionUID = -2799203888140748441L;

    @NaturalId
    private String userName;

    @OneToOne
    private Address address;

}
