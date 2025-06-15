package br.com.snapcast.port.local;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SalvarFramesTemporarioTest {

    private SalvarFramesTemporario salvarFrames;
    private BufferedImage testImage;
    private TestLogHandler logHandler;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setup() {
        salvarFrames = new SalvarFramesTemporario();
        testImage = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        // Preencher a imagem com uma cor para garantir que há dados
        var graphics = testImage.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 100, 100);
        graphics.dispose();

        logHandler = new TestLogHandler();
        Logger logger = Logger.getLogger(SalvarFramesTemporario.class.getName());
        logger.addHandler(logHandler);
    }

    @AfterEach
    void cleanup() {
        Logger logger = Logger.getLogger(SalvarFramesTemporario.class.getName());
        logger.removeHandler(logHandler);
    }

    @Test
    void deveSalvarFrameComSucesso() throws IOException {
        // given
        int frameNumber = 1;
        String outputPath = tempDir.toString();

        // when
        salvarFrames.salvarFrame(testImage, outputPath, frameNumber);

        // then
        Path expectedFile = tempDir.resolve("frame_1.png");
        assertThat(Files.exists(expectedFile)).isTrue();
        assertThat(logHandler.getLastMessage()).contains("Frame salvo em:");
    }

    @Test
    void deveLogarErroQuandoFalharAoSalvarFrame() {
        // given
        int frameNumber = 1;
        String invalidPath = "/invalid/path";

        // when
        salvarFrames.salvarFrame(testImage, invalidPath, frameNumber);

        // then
        assertThat(logHandler.getLastMessage()).contains("Erro ao salvar frame");
    }

    private static class TestLogHandler extends Handler {
        private LogRecord lastRecord;

        @Override
        public void publish(LogRecord record) {
            lastRecord = record;
        }

        public String getLastMessage() {
            return lastRecord != null ? lastRecord.getMessage() : null;
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }
}