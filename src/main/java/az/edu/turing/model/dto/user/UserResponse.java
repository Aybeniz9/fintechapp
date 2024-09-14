package az.edu.turing.model.dto.user;

import az.edu.turing.model.dto.account.AccountDto;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
@Data
public class UserResponse {
    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name cannot exceed 50 characters")
    private String name;

    @NotBlank(message = "Surname is required")
    @Size(max = 50, message = "Surname cannot exceed 50 characters")
    private String surname;

    @NotBlank(message = "FIN code is required")
    @Size(min = 7, max = 7, message = "FIN code must be exactly 10 characters")
    private String finCode;

    @Min(value = 18, message = "Age must be at least 18")
    private int age;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]" +
            "?)?\\d{10}$", message = "Invalid phone number format")
    private String phoneNumber;

    @Size(max = 255, message = "Address cannot exceed 255 characters")
    private String address;

    @NotBlank
    private List<AccountDto> accounts;
}
