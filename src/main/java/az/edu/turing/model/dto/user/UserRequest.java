package az.edu.turing.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {

    @NotBlank(message = "FIN code is required")
    @Size(min = 10, max = 10, message = "FIN code must be exactly 10 characters")
    private String finCode;

    @NotBlank
    private String password;
}
