package br.com.snapcast.port.local;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Path;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        extrairFrames = new ExtrairFrames(extrairImagem);
    }

    @Test
    void deveSepararFramesComSucesso() throws Exception {
        // Arrange
        String inputDir = "/path/to/video.mp4";
        Path tempPath = Path.of("/temp/path");
        int expectedFrames = 10;

        when(extrairImagem.processarFrames(any(FFmpegFrameGrabber.class),
                any(Java2DFrameConverter.class), any(Path.class)))
                .thenReturn(expectedFrames);

        // Act
        int actualFrames = extrairFrames.separarFramesVideo(inputDir, tempPath);

        // Assert
        assertThat(actualFrames).isEqualTo(expectedFrames);
        verify(extrairImagem).processarFrames(any(FFmpegFrameGrabber.class),
                any(Java2DFrameConverter.class), any(Path.class));
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