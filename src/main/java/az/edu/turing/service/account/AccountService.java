package az.edu.turing.service.account;

import az.edu.turing.dao.entity.AccountEntity;
import az.edu.turing.dao.entity.UserEntity;
import az.edu.turing.dao.repository.AccountRepository;
import az.edu.turing.dao.repository.UserRepository;
import az.edu.turing.exception.AccountBlockedException;
import az.edu.turing.exception.AccountsNotFoundException;
import az.edu.turing.exception.CartNotFoundException;
import az.edu.turing.exception.UserNotFoundException;
import az.edu.turing.mapper.AccountMapper;
import az.edu.turing.model.dto.account.AccountDto;
import az.edu.turing.model.dto.account.AccountTransferRequest;
import az.edu.turing.model.dto.account.AccountResponse;
import az.edu.turing.model.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final CartGenerationService cartGenerationService;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    public AccountTransferRequest transfer(AccountTransferRequest accountTransferRequest){

        AccountEntity accountEntity = accountRepository.findByCartNumber(accountTransferRequest.getCartNumber())
                .orElseThrow(() -> new CartNotFoundException("Cart not found !"));
        AccountStatus accountStatus = accountEntity.getAccountStatus();
        if (accountStatus == AccountStatus.BLOCKED) {
            throw new AccountBlockedException("Account blocked");
        }
        accountEntity.setBalance(accountTransferRequest.getPrice());
        return accountMapper.entityToAccountTransferRequest(accountEntity);
    }

    public AccountDto createAccount(String finCode) {
        UserEntity userEntity = userRepository.findByFinCode(finCode)
                .orElseThrow(() -> new UserNotFoundException("User not found with finCode: " + finCode));

        AccountDto cart = cartGenerationService.createCart();
        AccountEntity accountEntity = accountMapper.dtoToEntity(cart);
        accountEntity.setUser(userEntity);
        AccountEntity savedEntity = accountRepository.save(accountEntity);

        return accountMapper.entityToDto(savedEntity);
    }

    public void blockAccount(String cartNumber ){
        AccountEntity byCartNumber = accountRepository.findByCartNumber(cartNumber).orElseThrow(()->new CartNotFoundException("Cart not found"));
        byCartNumber.setAccountStatus(AccountStatus.BLOCKED);
        accountRepository.save(byCartNumber);
    }
    public List<AccountResponse> getBalance(String finCode) {
        UserEntity userEntity = userRepository.findByFinCode(finCode)
                .orElseThrow(() -> new UserNotFoundException("User not found with finCode: " + finCode));

        List<AccountEntity> accountsByUserId = accountRepository.findAccountsByUserId(userEntity.getId());

        if (accountsByUserId.isEmpty()) {
            throw new AccountsNotFoundException("No accounts found for user with finCode: " + finCode);
        }

        return accountMapper.entityToAccountResponse(accountsByUserId);
    }


}





