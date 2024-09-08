package az.edu.turing.model.dto.account;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountResponse {
    private String cartNumber;
    private BigDecimal price;
    private String status;
}
