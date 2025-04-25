package com.example.votacao.controller;

import com.example.votacao.model.Pauta;
import com.example.votacao.model.Voto;
import com.example.votacao.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PautaControllerPerformanceTest {

    @Autowired
    private PautaController pautaController;

    @Autowired
    private PautaRepository pautaRepository;

    private Pauta pauta;

    @BeforeEach
    public void setup() {
        // Configura uma pauta para os testes
        pauta = new Pauta();
        pauta.setTitulo("Teste de Performance");
        pauta.setSessaoAberta(true);
        pauta.setExpiracao(LocalDateTime.now().plusMinutes(10));
        pauta = pautaRepository.save(pauta);
    }

    @Test
    public void testPerformanceWithHighVotingLoad() throws InterruptedException {
        int numeroDeVotos = 100000; // Número de votos simulados
        ExecutorService executor = Executors.newFixedThreadPool(10); // Thread pool para concorrência

        // Submete os votos concorrentes
        for (int i = 1; i <= numeroDeVotos; i++) {
            final int associadoId = i; // Garante IDs únicos para os associados
            executor.submit(() -> {
                Voto voto = new Voto();
                voto.setAssociadoId(String.valueOf(associadoId));
                voto.setVoto("sim");
                ResponseEntity<?> response = pautaController.votar(pauta.getId(), String.valueOf(associadoId), voto);
                assertEquals(200, response.getStatusCodeValue());
            });
        }

        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.MINUTES); // Espera os votos serem processados

        // Verifica os resultados finais
        Pauta pautaFinal = pautaRepository.findById(pauta.getId()).orElseThrow();
        assertEquals(numeroDeVotos, pautaFinal.getVotos().size());
    }
}
