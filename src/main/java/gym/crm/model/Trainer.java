package gym.crm.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Trainer extends User{

    @ManyToOne
    private TrainingType specialization;

    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Training> trainings = new ArrayList<>();

    @ManyToMany(mappedBy = "trainers")
    @JsonIgnore
    private List<Trainee> trainees = new ArrayList<>();

    public void addTrainee(Trainee trainee) {
        trainees.add(trainee);
    }
}
