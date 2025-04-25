package com.example.votacao.controller;

import com.example.votacao.model.Pauta;
import com.example.votacao.model.Voto;
import com.example.votacao.repository.PautaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pautas") // Prefixo para todos os endpoints relacionados à votação
public class PautaController {

    @Autowired
    private PautaRepository pautaRepository;

    @GetMapping("/teste")
    public String teste() {
        return "Servidor está funcionando!";
    }

    @PostMapping
    public Pauta criarPauta(@RequestBody Pauta novaPauta) {
        return pautaRepository.save(novaPauta);
    }

    @PostMapping("/{id}/abrir-sessao")
    public ResponseEntity<?> abrirSessao(@PathVariable Long id, @RequestParam Optional<Integer> tempo) {
        Pauta pauta = pautaRepository.findById(id).orElse(null);
        if (pauta == null) return ResponseEntity.notFound().build();

        int tempoMinutos = tempo.orElse(1); // Tempo padrão de 1 minuto
        pauta.setSessaoAberta(true);
        pauta.setExpiracao(LocalDateTime.now().plusMinutes(tempoMinutos));
        pautaRepository.save(pauta);

        return ResponseEntity.ok(pauta);
    }

    @PostMapping("/{id}/votar")
    public ResponseEntity<?> votar(@PathVariable Long id, @RequestParam String cpf, @RequestBody Voto voto) {
        Pauta pauta = pautaRepository.findById(id).orElse(null);
        if (pauta == null || !pauta.isSessaoAberta() || LocalDateTime.now().isAfter(pauta.getExpiracao())) {
            return ResponseEntity.badRequest().body("Sessão encerrada ou pauta inválida.");
        }

        // Verificar se o associado pode votar com base no CPF
        String url = "https://user-info.herokuapp.com/users/" + cpf;
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, String> responseBody = response.getBody();

            if (responseBody != null && "UNABLE_TO_VOTE".equals(responseBody.get("status"))) {
                return ResponseEntity.badRequest().body("Associado não está habilitado para votar.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("CPF inválido ou API externa indisponível.");
        }

        // Verifica se o associado já votou
        boolean jaVotou = pauta.getVotos().stream()
                .anyMatch(v -> v.getAssociadoId().equals(voto.getAssociadoId()));
        if (jaVotou) return ResponseEntity.badRequest().body("Associado já votou nesta pauta.");

        pauta.getVotos().add(voto);
        pautaRepository.save(pauta);

        return ResponseEntity.ok(pauta);
    }

    @GetMapping("/{id}/resultado")
    public ResponseEntity<?> obterResultado(@PathVariable Long id) {
        Pauta pauta = pautaRepository.findById(id).orElse(null);
        if (pauta == null) return ResponseEntity.notFound().build();

        Map<String, Long> resultado = pauta.getVotos().stream()
                .collect(Collectors.groupingBy(Voto::getVoto, Collectors.counting()));

        return ResponseEntity.ok(Map.of(
                "titulo", pauta.getTitulo(),
                "resultado", resultado,
                "totalVotos", pauta.getVotos().size()
        ));
    }
}
