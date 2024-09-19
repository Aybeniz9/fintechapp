package az.edu.turing.service.account;

import az.edu.turing.dao.entity.AccountEntity;
import az.edu.turing.dao.entity.UserEntity;
import az.edu.turing.dao.repository.AccountRepository;
import az.edu.turing.dao.repository.UserRepository;
import az.edu.turing.exception.*;
import az.edu.turing.mapper.AccountMapper;
import az.edu.turing.model.dto.account.AccountDto;
import az.edu.turing.model.dto.account.AccountTransferRequest;
import az.edu.turing.model.dto.account.AccountResponse;
import az.edu.turing.model.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final CartGenerationService cartGenerationService;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountMapper accountMapper;

    @Transactional//todo
    public AccountTransferRequest transfer(String finCode ,AccountTransferRequest accountTransferRequest){
        Optional<UserEntity> myAccountEntity = userRepository.findByFinCode(finCode);
        List<AccountEntity> accounts = myAccountEntity.get().getAccounts();

        AccountEntity matchingAccount = accounts.stream()
                .filter(a -> a.getCartNumber().equals(accountTransferRequest.getMyCartNumber()))
                .findFirst().orElseThrow(()->new InvalidCartNumberException("Invalid cart number"));

        AccountEntity myAccount = accountRepository
                .findByCartNumber(accountTransferRequest.getMyCartNumber())
                .orElseThrow(() -> new InvalidCartNumberException("Cart number not found"));

        AccountEntity transferAccount = accountRepository
                .findByCartNumber(accountTransferRequest.getTransferCartNumber())
                .orElseThrow(() -> new CartNotFoundException("Cart number not found"));

        if (myAccount.getBalance().compareTo(accountTransferRequest.getBalance()) >=0){

            BigDecimal myCartNumberNewBalance = myAccount.getBalance().subtract(accountTransferRequest.getBalance());
            BigDecimal transferCartNumberNewBalance = transferAccount.getBalance().add(accountTransferRequest.getBalance());

            accountRepository.updateAccountBalance(myAccount.getCartNumber(),myCartNumberNewBalance);
            accountRepository.updateAccountBalance(transferAccount.getCartNumber(),transferCartNumberNewBalance);
        }
        return accountTransferRequest;
    }
    @Transactional
    public AccountDto createAccount(String finCode) {
        UserEntity userEntity = userRepository.findByFinCode(finCode)
                .orElseThrow(() -> new UserNotFoundException("User not found with finCode: " + finCode));

        AccountDto cart = cartGenerationService.createCart();
        AccountEntity accountEntity = accountMapper.dtoToEntity(cart);
        accountEntity.setUser(userEntity);
        AccountEntity savedEntity = accountRepository.save(accountEntity);

        return accountMapper.entityToDto(savedEntity);
    }
    @Transactional
    public void blockAccount(String cartNumber ){
        AccountEntity byCartNumber = accountRepository.findByCartNumber(cartNumber).orElseThrow(()->new CartNotFoundException("Cart not found"));
        byCartNumber.setAccountStatus(AccountStatus.BLOCKED);
        accountRepository.save(byCartNumber);
    }
    @Transactional(readOnly = true)
    public List<AccountResponse> getMyAccount(String finCode) {
        UserEntity userEntity = userRepository.findByFinCode(finCode)
                .orElseThrow(() -> new UserNotFoundException("User not found with finCode: " + finCode));

        List<AccountEntity> accountsByUserId = accountRepository.findAccountsByUserId(userEntity.getId());

        if (accountsByUserId.isEmpty()) {
            throw new AccountsNotFoundException("No accounts found for user with finCode: " + finCode);
        }

        return accountMapper.entityListToAccountResponseList(accountsByUserId);
    }
    @Transactional
    public void updatePin(String finCode,  String cartNumber , String oldPin , String newPin ) {
        UserEntity userEntity = userRepository.findByFinCode(finCode)
                .orElseThrow(() -> new UserNotFoundException("User not found with finCode: " + finCode));

        List<AccountEntity> accountsByUserId = accountRepository.findAccountsByUserId(userEntity.getId());
        AccountEntity matchesAccount = accountsByUserId.stream().filter(account -> account.getCartNumber().equals(cartNumber)).findFirst().orElseThrow(()->new CartNotFoundException("Cart not found"));

        boolean equals = matchesAccount.getPin().equals(oldPin);
        if (equals) {
            matchesAccount.setPin(newPin);
            accountRepository.updateAccountEntityByCartNumber(cartNumber, matchesAccount);
        }else {
            throw new InvalidCartNumberException("Pin code false ");
        }



    }

}






