# demoSBH
Example entity implementation that has a natural id. The use of the natural id of Hibernate allows code simplifications which can be made at the level of the 2 methods equals and hashCode. Using Lombok further increases the simplification. See en.lba.sbh.commons.AbstEntity.
all the intelligence is [here](https://github.com/laubaude/demoSBH/blob/master/src/main/java/fr/lba/sbh/commons/AbstEntity.java "AbstEntity.java").

[![Java CI with Maven](https://github.com/laubaude/demoSBH/actions/workflows/maven.yml/badge.svg)](https://github.com/laubaude/demoSBH/actions/workflows/maven.yml)


## Entity example

```java
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
```
