package br.com.snapcast.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import br.com.snapcast.criarObjeto.CriarVideoEvento;

public class ErroAoBaixarArquivoTest {

    @Test
    void verificarAoChamarConstrutoresCorretamente() {

        var erro1 = new ErroAoBaixarArquivo();

        assertThat(erro1).hasMessageContaining("Erro ao baixar arquivo do S3");

    }

    @Test
    void verificarAoChamarConstrutoreComParamentroCorretamente() {

        var video = CriarVideoEvento.criar();
        var throwable = mock(Throwable.class);

        when(throwable.getMessage()).thenReturn("Erro");
        var erro1 = new ErroAoBaixarArquivo(video, throwable);

        assertThat(erro1)
                .hasMessageContaining("Erro ao baixar arquivo do S3")
                .hasMessageContaining(video.nomeDoVideoComExtensao());

        verify(throwable, times(1)).getMessage();

    }
}