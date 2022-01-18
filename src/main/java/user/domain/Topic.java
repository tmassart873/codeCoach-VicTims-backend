package user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "topic_name")
    private String name;

    private Topic () {}

    private Topic (String name) {
        this.id = UUID.randomUUID();
        this.name = name;
    }
}
