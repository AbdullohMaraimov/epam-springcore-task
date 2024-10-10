package gym.crm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
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
@Entity
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "trainee_id", nullable = false)
    @JsonIgnore
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    @JsonIgnore
    private Trainer trainer;

    private String trainingName;

    @ManyToOne
    private TrainingType trainingType;

    private LocalDate trainingDate;

    private Duration duration;

}
