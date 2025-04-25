package dat.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dat.entities.Performance;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PerformanceDTO {

    private Integer id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String title;
    private float ticketPrice;
    private double latitude;
    private double longitude;
    private Performance.Genre genre;
    private Integer actorId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ActorDTO actor;

    public PerformanceDTO(Performance performance) {
        this(performance, false);
    }

    public PerformanceDTO(Performance performance, boolean withActorDetails){
        this.id = performance.getId();
        this.startTime = performance.getStartTime();
        this.endTime = performance.getEndTime();
        this.title = performance.getTitle();
        this.ticketPrice = performance.getTicketPrice();
        this.latitude = performance.getLatitude();
        this.longitude = performance.getLongitude();
        this.genre = performance.getGenre();
        this.actorId = performance.getActor() != null ? performance.getActor().getId() : null;

        if(withActorDetails){
            this.actor = performance.getActor() != null ? new ActorDTO(performance.getActor()) : null;
        }
    }

}
