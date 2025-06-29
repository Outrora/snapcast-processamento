package br.com.snapcast.event.consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import br.com.snapcast.domain.entities.VideoEvento;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.snapcast.criarObjeto.CriarVideoEvento;
import br.com.snapcast.domain.entities.StatusVideo;
import br.com.snapcast.domain.user_case.ProcessarVideoUserCase;
import br.com.snapcast.event.producer.AtualizarStatusEvent;

import java.util.concurrent.CompletableFuture;

class VideoUploadConsumerTest {

        @Mock
        ProcessarVideoUserCase processarVideoUserCase;

        @Mock
        AtualizarStatusEvent atualizarStatusEvent;

        VideoUploadConsumer consumer;

        AutoCloseable openMocks;

        @BeforeEach
        void init() {
                openMocks = MockitoAnnotations.openMocks(this);
                consumer = new VideoUploadConsumer(processarVideoUserCase, atualizarStatusEvent);
        }

        @AfterEach
        void tearDown() throws Exception {
                openMocks.close();
        }

        @Test
        void deveChamarOUseCaseCorretamente() throws Exception {
                Message<VideoEvento> mensagem = mock();
                var evento = CriarVideoEvento.criar();

                when(mensagem.ack()).thenReturn(CompletableFuture.completedFuture(null));
                when(mensagem.getPayload()).thenReturn(evento);
                doNothing()
                                .when(processarVideoUserCase)
                                .processarArquivo(any());

                consumer.receberVideo(mensagem);

                verify(processarVideoUserCase, times(1))
                                .processarArquivo(evento);
        }

        @Test
        void deveChamarUseCaseRetornarUmErro() throws Exception {
                Message<VideoEvento> mensagem = mock();
                var evento = CriarVideoEvento.criar();

                when(mensagem.getPayload()).thenReturn(evento);

                doThrow(new RuntimeException())
                                .when(processarVideoUserCase)
                                .processarArquivo(any());

                assertThatThrownBy(() -> consumer.receberVideo(mensagem))
                                .isInstanceOf(RuntimeException.class);

                verify(processarVideoUserCase, times(1))
                                .processarArquivo(evento);
        }

        @Test
        void deveChamarEnviarStatusNoMetodoFalharAoProcessar() {
                var evento = CriarVideoEvento.criar();

                doNothing()
                                .when(atualizarStatusEvent)
                                .enviarStatusVideo(any());

                consumer.falharAoProcessar(evento);

                ArgumentCaptor<StatusVideo> statusVideo = ArgumentCaptor.forClass(StatusVideo.class);

                verify(atualizarStatusEvent, times(1))
                                .enviarStatusVideo(statusVideo.capture());

                assertThat(statusVideo.getValue().id()).isEqualTo(evento.id());
                assertThat(statusVideo.getValue().quantidadeFrames()).isZero();
        }

}
