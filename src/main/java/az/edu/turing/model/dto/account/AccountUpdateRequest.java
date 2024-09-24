package az.edu.turing.model.dto.account;

import lombok.Data;

@Data
public class AccountUpdateRequest {
    private String cartNumber;
    private String oldPin;
    private String newPin;
}
