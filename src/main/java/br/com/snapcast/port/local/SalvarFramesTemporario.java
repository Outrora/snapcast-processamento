package br.com.snapcast.port.local;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@ApplicationScoped
@AllArgsConstructor(onConstructor = @__(@Inject))
@Log
public class SalvarFramesTemporario {
    private static final String FRAME_FORMAT = "png";
    private static final String FRAME_PREFIX = "frame_";
    private static final String FRAME_EXTENSION = ".png";

    public void salvarFrame(BufferedImage image, String outputDiretorio, int frameNumber) {
        Path outputPath = Paths.get(outputDiretorio, FRAME_PREFIX + frameNumber + FRAME_EXTENSION);
        try {
            ImageIO.write(image, FRAME_FORMAT, outputPath.toFile());
            log.log(Level.INFO, "💾 Frame salvo em: {0}", outputPath);
        } catch (IOException e) {
            log.log(Level.SEVERE, "⚠️ Erro ao salvar frame {0}: {1}", new Object[] { frameNumber, e.getMessage() });
        }
    }
}
