package br.com.snapcast.domain.user_case;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.snapcast.config.Configuracoes;
import br.com.snapcast.domain.user_case.ZiparArquivoUserCase;

@ExtendWith(MockitoExtension.class)
class ZipArquivoUserCaseTest {

    @Mock
    Configuracoes configuracoes;

    private ZiparArquivoUserCase userCase;

    @TempDir
    Path caminhoImagens;

    @TempDir
    Path lugarSalvarZip;

    @BeforeEach
    void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        userCase = new ZiparArquivoUserCase(configuracoes);
    }

    @Test
    void CriarArquivosZipComSucesso() throws IOException {

        String fileName = "test";
        Path file1 = caminhoImagens.resolve("file1.txt");
        Path file2 = caminhoImagens.resolve("file2.txt");
        Files.write(file1, "test1".getBytes());
        Files.write(file2, "test2".getBytes());

        when(configuracoes.getDiretorioVideos()).thenReturn(lugarSalvarZip.toString());

        String zipPath = userCase.ziparFrames(caminhoImagens.toString(), fileName);

        assertThat(Path.of(zipPath)).isRegularFile();

        verify(configuracoes).getDiretorioVideos();
    }

    @Test
    void CriarArquivoZipEmBranco() throws IOException {

        String fileName = "empty";
        when(configuracoes.getDiretorioVideos()).thenReturn(lugarSalvarZip.toString());

        String zipPath = userCase.ziparFrames(caminhoImagens.toString(), fileName);

        assertThat(Path.of(zipPath)).isRegularFile();
        verify(configuracoes).getDiretorioVideos();
    }
}