package br.com.snapcast.event.consumer;

import java.util.logging.Level;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import br.com.snapcast.domain.entities.StatusVideo;
import br.com.snapcast.domain.entities.VideoEvento;
import br.com.snapcast.domain.user_case.ProcessarVideoUserCase;
import br.com.snapcast.event.producer.AtualizarStatusEvent;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
@ApplicationScoped
@AllArgsConstructor(onConstructor = @__(@Inject))
public class VideoUploadConsumer {

    ProcessarVideoUserCase userCaseProcessar;

    AtualizarStatusEvent atualizarStatusEvent;

    @Incoming("video-uploads")
    @RunOnVirtualThread
    @Retry(delay = 10, maxRetries = 5)
    @Fallback(fallbackMethod = "falharAoProcessar")
    public void receberVideo(VideoEvento evento) throws Exception {
        log.info("🛬 Recebendo arquivo para Processar: %s".formatted(evento.nome()));
        try {
            userCaseProcessar.processarArquivo(evento);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }

    }

    public void falharAoProcessar(VideoEvento evento) {
        log.log(Level.SEVERE, "❌ Erro ao processar Arquivo");
        atualizarStatusEvent.enviarStatusVideo(StatusVideo.erroAoProcessar(evento.id()));
    }

}
