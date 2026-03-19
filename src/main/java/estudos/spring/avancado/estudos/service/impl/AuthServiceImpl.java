package estudos.spring.avancado.estudos.service.impl;

import estudos.spring.avancado.estudos.dto.request.LoginRequest;
import estudos.spring.avancado.estudos.dto.request.RegistroRequest;
import estudos.spring.avancado.estudos.dto.response.TokenResponse;
import estudos.spring.avancado.estudos.exception.EmailJaCadastradoException;
import estudos.spring.avancado.estudos.model.Usuario;
import estudos.spring.avancado.estudos.repository.UsuarioRepository;
import estudos.spring.avancado.estudos.security.JwtService;
import estudos.spring.avancado.estudos.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public TokenResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new EmailJaCadastradoException(request.email());
        }

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .build();

        usuarioRepository.save(usuario);
        log.info("Usuário registrado: {}", usuario.getEmail());

        UserDetails userDetails = new User(
                usuario.getEmail(),
                usuario.getSenha(),
                List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()))
        );

        String token = jwtService.gerarToken(userDetails);

        return TokenResponse.builder()
                .token(token)
                .tipo("Bearer")
                .email(usuario.getEmail())
                .role(usuario.getRole().name())
                .build();
    }

    @Override
    public TokenResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        var usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        UserDetails userDetails = new User(
                usuario.getEmail(),
                usuario.getSenha(),
                List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + usuario.getRole().name()))
        );

        String token = jwtService.gerarToken(userDetails);

        log.info("Login realizado: {}", usuario.getEmail());

        return TokenResponse.builder()
                .token(token)
                .tipo("Bearer")
                .email(usuario.getEmail())
                .role(usuario.getRole().name())
                .build();
    }
}
