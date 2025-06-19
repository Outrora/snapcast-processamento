package br.com.snapcast.port.s3;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.snapcast.config.ClienteS3;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

class EnviarArquivoTest {

    @Mock
    ClienteS3 s3;

    @Mock
    S3Client client;

    @TempDir
    Path temporario;

    EnviarArquivoS3 arquivoS3;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        arquivoS3 = new EnviarArquivoS3(s3);
    }

    @Test
    void verificarSeChamouOClienteS3Corretamente() {

        var caminho = "/temp/path";
        var bucket = "Bucket1";
        var regiao = "Regiao";

        when(s3.pegarS3()).thenReturn(client);
        when(s3.getBucket()).thenReturn(bucket);
        when(s3.getRegiao()).thenReturn(regiao);

        when(client.putObject(any(PutObjectRequest.class), any(Path.class)))
                .thenReturn(null);

        var retorno = arquivoS3.enviarArquivo(caminho, "test");

        assertThat(retorno)
                .contains(bucket)
                .contains(regiao)
                .contains("amazonaws");

    }

}
