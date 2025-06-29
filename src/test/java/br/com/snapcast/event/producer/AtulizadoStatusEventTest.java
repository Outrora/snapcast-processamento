package br.com.snapcast.event.producer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.snapcast.domain.entities.StatusVideo;

class AtulizadoStatusEventTest {

    @Mock
    private Emitter<StatusVideo> emitter;

    AutoCloseable openMocks;

    private AtualizarStatusEvent event;

    @BeforeEach
    void init() {
        openMocks = MockitoAnnotations.openMocks(this);
        event = new AtualizandoStatusEventImpl(emitter);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveEnviarUmMensagemCorretamente() {
        var videoProcessado = StatusVideo.processamentoConcluido(UUID.randomUUID().toString(), 050);

        when(emitter.send(any(StatusVideo.class))).thenReturn(CompletableFuture.completedFuture(null));
        event.enviarStatusVideo(videoProcessado);

        verify(emitter, times(1)).send(videoProcessado);

    }

}
