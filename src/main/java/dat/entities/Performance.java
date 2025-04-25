package dat.entities;

import dat.dtos.PerformanceDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "performance")
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "performance_id", nullable = false, unique = true)
    private Integer id;

    @Setter
    @Column(name = "performance_starttime", nullable = false, unique = false)
    private LocalDateTime startTime;

    @Setter
    @Column(name = "performance_endtime", nullable = false, unique = false)
    private LocalDateTime endTime;

    @Setter
    @Column(name = "performance_title", nullable = false, unique = false)
    private String title;

    @Setter
    @Column(name = "performance_price", nullable = false, unique = false)
    private float ticketPrice;

    @Setter
    @Column(name = "performance_latitude", nullable = false, unique = false)
    private double latitude;

    @Setter
    @Column(name = "performance_longitude", nullable = false, unique = false)
    private double longitude;

    @Setter
    @Column(name = "performance_genre", nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @Setter
    @ManyToOne
    @JoinColumn(name = "actor_id", nullable = true)
    private Actor actor;

    public Performance(PerformanceDTO performanceDTO) {
        this.id = performanceDTO.getId();
        this.startTime = performanceDTO.getStartTime();
        this.endTime = performanceDTO.getEndTime();
        this.title = performanceDTO.getTitle();
        this.ticketPrice = performanceDTO.getTicketPrice();
        this.latitude = performanceDTO.getLatitude();
        this.longitude = performanceDTO.getLongitude();
        this.genre = performanceDTO.getGenre();
    }

    public enum Genre {
        DRAMA, COMEDY, MUSICAL
    }
}
