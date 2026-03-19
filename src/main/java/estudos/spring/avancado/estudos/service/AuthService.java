package estudos.spring.avancado.estudos.service;

import estudos.spring.avancado.estudos.dto.request.LoginRequest;
import estudos.spring.avancado.estudos.dto.request.RegistroRequest;
import estudos.spring.avancado.estudos.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse registrar(RegistroRequest request);
    TokenResponse login(LoginRequest request);
}
