package com.opentrainer.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private List<RegistrationOption> registrationOptions;
    private boolean active;

    public void addRegistrationOption(RegistrationOption option) {
        this.registrationOptions.add(option);
    }
}
