package com.example.votacao.controller.mock;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
public class MockController {

    // CPFs válidos (ABLE_TO_VOTE ou UNABLE_TO_VOTE)
    private static final Map<String, String> CPFS_VALIDOS = Map.of(
            "11111111111", "ABLE_TO_VOTE",
            "22222222222", "ABLE_TO_VOTE",
            "33333333333", "UNABLE_TO_VOTE",
            "44444444444", "UNABLE_TO_VOTE",
            "55555555555", "ABLE_TO_VOTE"
    );

    // CPFs inválidos
    private static final Set<String> CPFS_INVALIDOS = Set.of(
            "00000000000",
            "12345678901",
            "98765432100",
            "99999999999",
            "88888888888"
    );

    @GetMapping("/users/{cpf}")
    public ResponseEntity<?> verificarCpf(@PathVariable String cpf) {
        if (CPFS_VALIDOS.containsKey(cpf)) {
            return ResponseEntity.ok(Map.of("status", CPFS_VALIDOS.get(cpf)));
        } else if (CPFS_INVALIDOS.contains(cpf)) {
            return ResponseEntity.status(404).body(Map.of("message", "CPF inválido"));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "CPF não encontrado"));
        }
    }
}
