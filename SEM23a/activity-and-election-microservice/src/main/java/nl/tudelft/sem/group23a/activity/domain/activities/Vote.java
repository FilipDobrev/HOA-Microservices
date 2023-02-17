package nl.tudelft.sem.group23a.activity.domain.activities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A DDD value object representing the vote in the domain.
 */
@Entity
@Table(name = "votes")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vote {

    /**
     * Identifier for the vote entity.
     */
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private long id;

    @Getter
    @Column(name = "username", nullable = false)
    private String username;

    @Getter
    @Column(name = "choice", nullable = false)
    private String choice;

    @ManyToOne
    @JoinColumn(name = "activities", nullable = false)
    private Activity activity;
}
