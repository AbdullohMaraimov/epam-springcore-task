package gym.crm.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class User {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private Boolean isActive;

}
