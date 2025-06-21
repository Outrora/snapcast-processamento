package br.com.snapcast.domain.user_case;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.snapcast.port.local.SalvarFramesTemporario;

class ExtrairImagemUseCaseTest {
    @Mock
    private SalvarFramesTemporario salvarFrames;

    @Mock
    private FFmpegFrameGrabber grabber;

    @Mock
    private Java2DFrameConverter converter;

    @Mock
    private Frame frame;

    @Mock
    private BufferedImage bufferedImage;

    private ExtrairImagemUserCase userCase;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        userCase = new ExtrairImagemUserCase(salvarFrames);
    }

    @Test
    void deveProcessarFramesComSucesso() throws IOException {
        // Arrange
        when(grabber.grabImage())
                .thenReturn(frame)
                .thenReturn(frame)
                .thenReturn(frame)
                .thenReturn(frame)
                .thenReturn(frame)
                .thenReturn(frame)
                .thenReturn(frame)
                .thenReturn(frame)
                .thenReturn(frame)
                .thenReturn(frame)
                .thenReturn(null);

        when(converter.convert(frame)).thenReturn(bufferedImage);

        // Act
        int result = userCase.processarFrames(grabber, converter, tempDir);

        // Assert
        assertThat(result).isEqualTo(1);
        verify(salvarFrames, times(1)).salvarFrame(eq(bufferedImage), anyString(), any(Integer.class));
    }

    @Test
    void deveIgnorarFramesNulos() throws IOException {
        // Arrange
        when(grabber.grabImage())
                .thenReturn(frame) // frame 1
                .thenReturn(frame) // frame 2
                .thenReturn(frame) // frame 3
                .thenReturn(frame) // frame 4
                .thenReturn(frame) // frame 5
                .thenReturn(frame) // frame 6
                .thenReturn(frame) // frame 7
                .thenReturn(frame) // frame 8
                .thenReturn(frame) // frame 9
                .thenReturn(frame) // frame 10
                .thenReturn(frame) // frame 11
                .thenReturn(null);

        when(converter.convert(frame)).thenReturn(null);

        // Act
        int result = userCase.processarFrames(grabber, converter, tempDir);

        // Assert
        assertThat(result).isZero();
        verify(salvarFrames, times(0)).salvarFrame(any(), anyString(), any(Integer.class));
    }

    @Test
    void deveProcessarFramesConsiderandoFrameSkip() throws IOException {
        // Arrange
        when(grabber.grabImage())
                .thenReturn(frame) // frame 1
                .thenReturn(frame) // frame 2
                .thenReturn(frame) // frame 3
                .thenReturn(frame) // frame 4
                .thenReturn(frame) // frame 5
                .thenReturn(frame) // frame 6
                .thenReturn(frame) // frame 7
                .thenReturn(frame) // frame 8
                .thenReturn(frame) // frame 9
                .thenReturn(frame) // frame 10
                .thenReturn(frame) // frame 11
                .thenReturn(null);

        when(converter.convert(frame)).thenReturn(bufferedImage);

        // Act
        int result = userCase.processarFrames(grabber, converter, tempDir);

        // Assert
        assertThat(result).isEqualTo(1);
        verify(salvarFrames, times(1)).salvarFrame(eq(bufferedImage), anyString(), eq(10));
    }
}
