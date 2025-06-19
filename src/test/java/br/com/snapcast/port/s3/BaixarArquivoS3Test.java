
package br.com.snapcast.port.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import br.com.snapcast.config.ClienteS3;
import br.com.snapcast.config.Configuracoes;
import br.com.snapcast.criarObjeto.CriarVideoEvento;
import br.com.snapcast.domain.entities.VideoEvento;
import br.com.snapcast.exception.ErroAoBaixarArquivo;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

class BaixarArquivoS3Test {

    @Mock
    private Configuracoes config;

    @Mock
    private ClienteS3 s3;

    @Mock
    private S3Client s3Client;

    private BaixarArquivoS3 baixarArquivo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        baixarArquivo = new BaixarArquivoS3(config, s3);
        when(s3.pegarS3()).thenReturn(s3Client);
        when(s3.getBucket()).thenReturn("test-bucket");
    }

    @Test
    void deveBaixarArquivoComSucesso() throws IOException {
        // given
        VideoEvento video = CriarVideoEvento.criar();
        when(config.getDiretorioVideos()).thenReturn("/tmp");

        ResponseInputStream<GetObjectResponse> mockStream = new ResponseInputStream<>(
                GetObjectResponse.builder().build(),
                new ByteArrayInputStream("test data".getBytes()));
        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(mockStream);

        // when
        String result = baixarArquivo.baixarArquivo(video);

        // then
        assertThat(result).contains(video.id())
                .contains(video.formatoVideo())
                .contains(video.nomeDoVideoComExtensao());

    }

    @Test
    void deveLancarExcecaoQuandoOcorrerErro() {
        // given
        VideoEvento video = CriarVideoEvento.criar();
        when(config.getDiretorioVideos()).thenReturn("/invalid/path");

        ResponseInputStream<GetObjectResponse> mockStream = new ResponseInputStream<>(
                GetObjectResponse.builder().build(),
                new ByteArrayInputStream("test data".getBytes()));

        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(mockStream);

        assertThatThrownBy(() -> baixarArquivo.baixarArquivo(video))
                .isInstanceOf(ErroAoBaixarArquivo.class);
    }
}
