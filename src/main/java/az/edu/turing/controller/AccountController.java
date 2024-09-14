package az.edu.turing.controller;

import az.edu.turing.model.dto.account.AccountDto;
import az.edu.turing.model.dto.account.AccountTransferRequest;
import az.edu.turing.model.dto.account.AccountResponse;
import az.edu.turing.service.account.AccountService;
import az.edu.turing.service.authorization.AuthorizationHelperService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService service;
    private final AuthorizationHelperService helper;
    @PostMapping("/transfer")
    public ResponseEntity<AccountTransferRequest> transferMoney(@Valid @RequestBody AccountTransferRequest requestDto) {
        return ResponseEntity.ok(service.transfer(requestDto));
    }
    @PostMapping("/create")
    public ResponseEntity<AccountDto> create(@RequestHeader ("Authorization") String auth) {//TODO
        String finCode = helper.getFinCode(auth);
        return ResponseEntity.ok(service.createAccount(finCode));
    }
    @GetMapping("/balance")
    public ResponseEntity<List<AccountResponse>> getBalance(@RequestHeader ("Authorization") String auth) {
        String finCode = helper.getFinCode(auth);
        return ResponseEntity.ok(service.getBalance(finCode));
    }
    @PatchMapping("/update/block/{cartNumber}")
    public void update(@PathVariable String cartNumber) {
        service.blockAccount(cartNumber);
    }
}
