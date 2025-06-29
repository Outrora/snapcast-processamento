package br.com.snapcast.event.consumer;

import java.util.concurrent.CompletionStage;
import java.util.logging.Level;

import org.eclipse.microprofile.faulttolerance.Bulkhead;
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
import org.eclipse.microprofile.reactive.messaging.Message;

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
    @Bulkhead(value = 2)
    public CompletionStage<Void> receberVideo(Message<VideoEvento> mensagem) throws Exception {
        VideoEvento evento = mensagem.getPayload();
        log.info("🛬 Recebendo arquivo para Processar: %s".formatted(evento.nome()));
        try {
            userCaseProcessar.processarArquivo(evento);
            return mensagem.ack();
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage());
            throw e;
        }

    }

    public CompletionStage<Void> falharAoProcessar(Message<VideoEvento> mensagem) {
        VideoEvento evento = mensagem.getPayload();
        log.log(Level.SEVERE, "❌ Erro ao processar Arquivo");
        atualizarStatusEvent.enviarStatusVideo(StatusVideo.erroAoProcessar(evento.id()));
        return mensagem.ack();
    }

}
