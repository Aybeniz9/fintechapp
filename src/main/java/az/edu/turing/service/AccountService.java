package az.edu.turing.service;

import az.edu.turing.dao.entity.AccountEntity;
import az.edu.turing.dao.entity.UserEntity;
import az.edu.turing.dao.repository.AccountRepository;
import az.edu.turing.dao.repository.UserRepository;
import az.edu.turing.exception.AccountBlockedException;
import az.edu.turing.exception.CartNotFoundException;
import az.edu.turing.exception.UserNotFoundException;
import az.edu.turing.mapper.AccountMapper;
import az.edu.turing.mapper.AccountRequestMapper;
import az.edu.turing.mapper.AccountResponseMapper;
import az.edu.turing.model.dto.account.AccountDto;
import az.edu.turing.model.dto.account.AccountRequest;
import az.edu.turing.model.dto.account.AccountResponse;
import az.edu.turing.model.enums.AccountStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    private final CartGenerationService cartGenerationService;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountRequestMapper accountRequestMapper;
    private final AccountResponseMapper accountResponseMapper;
    private final AccountMapper accountMapper;

    public AccountRequest transfer(AccountRequest accountRequest){

        AccountEntity accountEntity = accountRepository.findByCartNumber(accountRequest.getCartNumber())
                .orElseThrow(() -> new CartNotFoundException("Cart not found !"));
        AccountStatus accountStatus = accountEntity.getAccountStatus();
        if (accountStatus == AccountStatus.BLOCKED) {
            throw new AccountBlockedException("Account blocked");
        }
        accountEntity.setBalance(accountRequest.getPrice());
        return accountRequestMapper.entityToDto(accountEntity);
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
    public AccountResponse getBalance(String cartNumber) {
        AccountEntity account = accountRepository.findByCartNumber(cartNumber).orElseThrow(() -> new CartNotFoundException("Cart not found !"));
        return   accountResponseMapper.entityToDto(account);
    }

}





