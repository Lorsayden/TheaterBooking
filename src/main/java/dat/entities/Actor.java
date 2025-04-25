package dat.entities;

import dat.dtos.ActorDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "actor")
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actor_id", nullable = false, unique = true)
    int id;

    @Setter
    @Column(name = "actor_firstname", nullable = false, unique = false)
    String firstName;

    @Setter
    @Column(name = "actor_lastname", nullable = false, unique = false)
    String lastName;

    @Setter
    @Column(name = "actor_email", nullable = false, unique = true)
    String email;

    @Setter
    @Column(name = "actor_phone", nullable = false, unique = true)
    Integer phone;

    @Setter
    @Column(name = "actor_yearsofexperience", nullable = false, unique = false)
    int yearsOfExperience;

    @Setter
    @OneToMany(mappedBy = "actor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Performance> performances = new HashSet<>();

    public Actor(ActorDTO actorDTO) {
        this.id = actorDTO.getId();
        this.firstName = actorDTO.getFirstName();
        this.lastName = actorDTO.getLastName();
        this.email = actorDTO.getEmail();
        this.phone = actorDTO.getPhone();
        this.yearsOfExperience = actorDTO.getYearsOfExperience();
    }
}
