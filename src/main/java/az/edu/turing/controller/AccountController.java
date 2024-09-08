package az.edu.turing.controller;

import az.edu.turing.model.dto.account.AccountDto;
import az.edu.turing.model.dto.account.AccountRequest;
import az.edu.turing.model.dto.account.AccountResponse;
import az.edu.turing.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;

    @PostMapping("/transfer")
    public ResponseEntity<AccountRequest> transferMoney(@Valid @RequestBody AccountRequest requestDto) {
        return ResponseEntity.ok(service.transfer(requestDto));
    }
    @PostMapping("/create/{finCode}")
    public ResponseEntity<AccountDto> create(@PathVariable String finCode) {//todo
        return ResponseEntity.ok(service.createAccount(finCode));
    }
    @GetMapping("/balance/{cartNumber}")
    public ResponseEntity<AccountResponse> getBalance(@PathVariable String cartNumber) {
        return ResponseEntity.ok(service.getBalance(cartNumber));
    }
    @PatchMapping("/update/block/{cartNumber}")
    public void update(@PathVariable String cartNumber) {
        service.blockAccount(cartNumber);
    }
}
