package gym_crm.model;

import lombok.*;

@Getter
@Setter
public abstract class User {

    private String firstName;

    private String lastName;

    private String username;

    private String password;

    private Boolean isActive;

}
