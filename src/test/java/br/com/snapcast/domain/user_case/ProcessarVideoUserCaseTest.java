package br.com.snapcast.domain.user_case;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.snapcast.config.Configuracoes;
import br.com.snapcast.criarObjeto.CriarVideoEvento;
import br.com.snapcast.event.producer.AtualizarStatusEvent;
import br.com.snapcast.exception.ErroAoProcessarArquivo;
import br.com.snapcast.exception.ErroAoProcessarArquivoTest;
import br.com.snapcast.port.BaixarArquivo;
import br.com.snapcast.port.EnviarArquivo;
import br.com.snapcast.port.local.ExtrairFrames;

class ProcessarVideoUserCaseTest {

    @Mock
    private ExtrairFrames extrairImagem;
    @Mock
    private ZiparArquivoUserCase ziparArquivo;
    @Mock
    private BaixarArquivo baixarArquivo;
    @Mock
    private EnviarArquivo enviarArquivo;
    @Mock
    private AtualizarStatusEvent atualizarStatus;
    @Mock
    private Configuracoes configuracoes;

    @TempDir
    Path tempDir;

    private ProcessarVideoUserCase userCase;

    AutoCloseable openMocks;

    @BeforeEach
    void init() {
        openMocks = MockitoAnnotations.openMocks(this);
        userCase = new ProcessarVideoUserCase(extrairImagem, ziparArquivo, baixarArquivo, enviarArquivo,
                atualizarStatus, configuracoes);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void deveProcessarCorretamete() throws Exception {

        var video = CriarVideoEvento.criar();
        when(configuracoes.getDiretorioVideos()).thenReturn(tempDir.toString());
        when(baixarArquivo.baixarArquivo(any())).thenReturn(tempDir.toString());
        when(extrairImagem.separarFramesVideo(anyString(), any())).thenReturn(50);
        when(ziparArquivo.ziparFrames(anyString(), anyString())).thenReturn(tempDir.toString());
        when(enviarArquivo.enviarArquivo(anyString(), anyString())).thenReturn("....");
        doNothing().when(atualizarStatus).enviarStatusVideo(any());

        userCase.processarArquivo(video);

        verify(configuracoes, times(1)).getDiretorioVideos();
        verify(baixarArquivo, times(1)).baixarArquivo(any());
        verify(extrairImagem, times(1)).separarFramesVideo(anyString(), any());
        verify(ziparArquivo, times(1)).ziparFrames(anyString(), anyString());
        verify(enviarArquivo, times(1)).enviarArquivo(anyString(), anyString());
        verify(atualizarStatus, times(1)).enviarStatusVideo(any());

    }

    @Test
    void naoDeveProcessarCorretameteERetornarErroAoProcessarArquivo() throws Exception {

        var video = CriarVideoEvento.criar();
        when(configuracoes.getDiretorioVideos()).thenReturn(tempDir.toString());
        when(baixarArquivo.baixarArquivo(any())).thenReturn(tempDir.toString());
        when(extrairImagem.separarFramesVideo(anyString(), any())).thenThrow(new ErroAoProcessarArquivo());
        when(ziparArquivo.ziparFrames(anyString(), anyString())).thenReturn(tempDir.toString());
        when(enviarArquivo.enviarArquivo(anyString(), anyString())).thenReturn("....");
        doNothing().when(atualizarStatus).enviarStatusVideo(any());

        assertThatThrownBy(() -> userCase.processarArquivo(video))
                .isInstanceOf(ErroAoProcessarArquivo.class)
                .hasMessageContaining("Erro ao processar o arquivo");

        verify(configuracoes, times(1)).getDiretorioVideos();
        verify(baixarArquivo, times(1)).baixarArquivo(any());
        verify(extrairImagem, times(1)).separarFramesVideo(anyString(), any());
        verify(ziparArquivo, never()).ziparFrames(anyString(), anyString());
        verify(enviarArquivo, never()).enviarArquivo(anyString(), anyString());

    }

}
