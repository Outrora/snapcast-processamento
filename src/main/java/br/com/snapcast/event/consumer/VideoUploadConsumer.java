package br.com.snapcast.event.consumer;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import br.com.snapcast.domain.entities.VideoEvento;
import br.com.snapcast.domain.user_case.ProcessarVideoUserCase;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
@ApplicationScoped
@AllArgsConstructor(onConstructor = @__(@Inject))
public class VideoUploadConsumer {

    ProcessarVideoUserCase userCaseProcessar;

    @Incoming("video-uploads")
    @Blocking
    public void receberVideo(VideoEvento evento) throws Exception {
        log.info("🛬 Recebendo arquivo para Processar: %s".formatted(evento.nome()));
        userCaseProcessar.processarArquivo(evento);
    }

}
