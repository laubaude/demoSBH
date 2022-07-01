package fr.lba.sbh.model;

import javax.persistence.Entity;

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
public class Address extends AbstEntity {

    private static final long serialVersionUID = -9021955304306833845L;

    @NaturalId
    private String street;

    @NaturalId
    private String city;

    @NaturalId
    private String state;

    @NaturalId
    private String zip;

}
