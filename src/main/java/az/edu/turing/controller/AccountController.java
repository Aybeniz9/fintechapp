package az.edu.turing.controller;

import az.edu.turing.model.dto.account.AccountDto;
import az.edu.turing.model.dto.account.AccountTransferRequest;
import az.edu.turing.model.dto.account.AccountResponse;
import az.edu.turing.model.dto.account.AccountUpdateRequest;
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
    public ResponseEntity<AccountTransferRequest> transferMoney(@RequestHeader("Authorization") String auth, @Valid @RequestBody AccountTransferRequest requestDto) {
        String finCode = helper.getFinCode(auth);
        return ResponseEntity.ok(service.transfer(finCode, requestDto));
    }

    @PostMapping("/create")
    public ResponseEntity<AccountDto> create(@RequestHeader("Authorization") String auth) {
        String finCode = helper.getFinCode(auth);
        return ResponseEntity.ok(service.createAccount(finCode));
    }

    @GetMapping("/")
    public ResponseEntity<List<AccountResponse>> account(@RequestHeader("Authorization") String auth) {
        String finCode = helper.getFinCode(auth);
        return ResponseEntity.ok(service.getMyAccount(finCode));
    }

    @PatchMapping("/update")
    public void update(@RequestHeader("Authorization") String auth, @RequestBody AccountUpdateRequest request) {
        String finCode = helper.getFinCode(auth);
        service.updatePin(finCode, request.getCartNumber(), request.getOldPin(), request.getNewPin());
    }
}