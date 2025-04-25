package dat.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PropDTO {
    private String name;
    private String description;
    private String genre;
    private int costEstimate;
    private String createdAt;
    private String updatedAt;
}
