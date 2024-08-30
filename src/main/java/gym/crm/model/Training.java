package gym.crm.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Training {

    private String id;

    private String traineeId;

    private String trainerId;

    private String trainingName;

    private TrainingType trainingType = TrainingType.STANDARD;

    private LocalDate trainingDate;

    private Duration duration;

}
