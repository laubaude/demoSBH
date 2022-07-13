package fr.lba.sbh.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

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
public class Order extends AbstEntity {

    private static final long serialVersionUID = -2970059177687244588L;

    @ManyToOne
    private Pet pet;

    @NaturalId
    private String id = UUID.randomUUID().toString();

    @ManyToOne
    private Customer customer;

    private LocalDate shipDate;

    @Enumerated(javax.persistence.EnumType.STRING)
    private Status status;

    private Boolean complete;

}
