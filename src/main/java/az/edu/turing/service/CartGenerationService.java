package az.edu.turing.service;

import az.edu.turing.model.dto.account.AccountDto;
import az.edu.turing.model.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;
@Service
@RequiredArgsConstructor
public class CartGenerationService {

    private final Random random =new Random();

    public AccountDto createCart() {
        AccountDto accountDto = new AccountDto(); // Yenisini yaratmaq
        accountDto.setCartNumber(cartGenerator());
        accountDto.setAccountStatus(AccountStatus.ACTIVE);
        accountDto.setBalance(BigDecimal.valueOf(0));
        accountDto.setCvv(cvvGenerator());
        accountDto.setPin(pinGenerator());
        return accountDto; // Yaradılan account-u geri qaytarmaq
    }

    private String cartGenerator() {
        return "4098" + (long)(random.nextDouble() * 9_000_000_000_00L);
    }

    private String cvvGenerator() {
        return String.valueOf(100 + random.nextInt(900));
    }

    private String pinGenerator() {
        return String.valueOf(1000 + random.nextInt(9000));
    }


}