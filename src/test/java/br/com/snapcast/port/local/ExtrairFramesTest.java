package br.com.snapcast.port.local;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import br.com.snapcast.domain.user_case.ExtrairImagemUserCase;
import br.com.snapcast.exception.ErroAoProcessarArquivo;

@ExtendWith(MockitoExtension.class)
class ExtrairFramesTest {

    @Mock
    private ExtrairImagemUserCase extrairImagem;

    private ExtrairFrames extrairFrames;

    private File videoFile;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() throws IOException {
        MockitoAnnotations.openMocks(this);
        extrairFrames = new ExtrairFrames(extrairImagem);

        // Criar um arquivo de vídeo de teste
        videoFile = new File(tempDir.toFile(), "test-video.mp4");
        // Copiar um vídeo de teste dos recursos
        try (InputStream is = getClass().getResourceAsStream("/test-video.mp4");
                FileOutputStream fos = new FileOutputStream(videoFile)) {
            if (is == null) {
                throw new IOException("Arquivo de vídeo de teste não encontrado");
            }
            is.transferTo(fos);
        }
    }

    @Test
    void deveSepararFramesComSucesso() throws Exception {
        // Arrange
        String inputDir = videoFile.getAbsolutePath();
        int expectedFrames = 10;

        when(extrairImagem.processarFrames(any(FFmpegFrameGrabber.class),
                any(Java2DFrameConverter.class),
                eq(tempDir)))
                .thenReturn(expectedFrames);

        // Act
        int actualFrames = extrairFrames.separarFramesVideo(inputDir, tempDir);

        // Assert
        assertThat(actualFrames).isEqualTo(expectedFrames);
        verify(extrairImagem).processarFrames(any(FFmpegFrameGrabber.class),
                any(Java2DFrameConverter.class),
                eq(tempDir));
    }

    @Test
    void deveLancarExcecaoQuandoOcorrerErro() {
        // Arrange
        String inputDir = "/path/to/video.mp4";
        Path tempPath = Path.of("/temp/path");

        // Act & Assert
        assertThatThrownBy(() -> extrairFrames.separarFramesVideo(inputDir, tempPath))
                .isInstanceOf(ErroAoProcessarArquivo.class);
    }
}