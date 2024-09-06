package gym.crm.model;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Entity
public class Trainee extends User{

    private LocalDate dateOfBirth;

    private String address;

    private UUID userId;

}
