package br.com.snapcast.event.producer;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import br.com.snapcast.domain.entities.StatusVideo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.java.Log;

@ApplicationScoped
@Log
public class AtualizandoStatusEventImpl implements AtualizarStatusEvent {

    private Emitter<StatusVideo> emitter;

    @Inject
    public AtualizandoStatusEventImpl(
            @Channel("video-status") Emitter<StatusVideo> emitter) {
        this.emitter = emitter;
    }

    @Override
    @Retry(maxRetries = 4, delay = 10)
    public void enviarStatusVideo(StatusVideo video) {
        log.info("♻️ Atualizando Status do Processamento");

        emitter.send(video)
                .thenRun(() -> log.info("🛫 Enviado para a Fila -> video-status"))
                .toCompletableFuture()
                .join();

    }

}
