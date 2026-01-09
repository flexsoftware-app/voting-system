package pl.kkaczynski.votingweb.voter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VoterCreateRequest(

        @Email(message = "Email must be valid")
        @Size(max = 300, message = "Email must not exceed 300 characters")
        String email,

        @NotBlank(message = "First name is required")
        @Size(max = 120, message = "First name must not exceed 120 characters")
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 120, message = "Last name must not exceed 120 characters")
        String lastName
) {
}