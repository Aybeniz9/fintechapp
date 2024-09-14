package az.edu.turing.service.authorization;

import az.edu.turing.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthorizationHelperService {
    private final JwtService jwtService;

    public String getFinCode(String authorization) {
        String token;
        String finCode;
        token = authorization.substring(7);
        finCode = jwtService.extractUsername(token);
        return finCode;
    }
}
