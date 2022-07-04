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

## Run & test
- $ mvn spring-boot:run &
- $ curl -X POST http://localhost:8080/api/pet/get -H 'Content-Type: application/json' -d '{"name":"Rex"}'

```
i.StatisticalLoggingSessionEventListener : Session Metrics {
    453700 nanoseconds spent acquiring 1 JDBC connections;
    0 nanoseconds spent releasing 0 JDBC connections;
    0 nanoseconds spent preparing 0 JDBC statements;
    0 nanoseconds spent executing 0 JDBC statements;
    0 nanoseconds spent executing 0 JDBC batches;
    0 nanoseconds spent performing 0 L2C puts;
    273900 nanoseconds spent performing 3 L2C hits;
    0 nanoseconds spent performing 0 L2C misses;
    0 nanoseconds spent executing 0 flushes (flushing a total of 0 entities and 0 collections);
    0 nanoseconds spent executing 0 partial-flushes (flushing a total of 0 entities and 0 collections)
}
```

- Entity it's easy to write (only functional anotations), and you can saw that no sql statement execution, only cahe hits.
