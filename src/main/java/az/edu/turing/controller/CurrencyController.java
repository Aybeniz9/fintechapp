package az.edu.turing.controller;

import az.edu.turing.model.dto.currency.CurrencyDto;
import az.edu.turing.service.authorization.AuthorizationHelperService;
import az.edu.turing.service.currency.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/currency")
@RequiredArgsConstructor
@RestController
public class CurrencyController {

    private final AuthorizationHelperService helper;
    private final CurrencyService currencyService;

    @GetMapping("/euro")
    public ResponseEntity<CurrencyDto> getEuroCurrency() {
        return ResponseEntity.ok(currencyService.euro());
    }

    @GetMapping("/usd")
    public ResponseEntity<CurrencyDto> getUsdCurrency() {
        return ResponseEntity.ok(currencyService.euro());
    }

    @GetMapping("/trl")
    public ResponseEntity<CurrencyDto> getTlCurrency() {
        return ResponseEntity.ok(currencyService.trl());
    }
}
