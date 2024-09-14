package az.edu.turing.exception;

public class AccountsNotFoundException extends RuntimeException {
    public AccountsNotFoundException(String message) {
        super(message);
    }
}
