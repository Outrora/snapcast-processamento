package br.com.snapcast.event.consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        var evento = CriarVideoEvento.criar();

        doNothing()
                .when(processarVideoUserCase)
                .processarArquivo(any());

        consumer.receberVideo(evento);

        verify(processarVideoUserCase, times(1))
                .processarArquivo(evento);
    }

    @Test
    void deveChamarUseCaseRetornarUmErro() throws Exception {
        var evento = CriarVideoEvento.criar();

        doThrow(new RuntimeException())
                .when(processarVideoUserCase)
                .processarArquivo(any());

        assertThatThrownBy(() -> consumer.receberVideo(evento))
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
        assertThat(statusVideo.getValue().quantidadeFrames()).isEqualTo(0);
    }

}
