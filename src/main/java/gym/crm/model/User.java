package gym.crm.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class User {

    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private Boolean isActive;

}
