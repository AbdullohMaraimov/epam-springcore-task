package gym_crm.model;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trainee extends User{

    private LocalDate dateOfBirth;

    private String address;

    private UUID userId;

}
