package br.com.snapcast.domain.user_case;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import br.com.snapcast.domain.port.local.SalvarFramesTemporario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@ApplicationScoped
@Log
@AllArgsConstructor(onConstructor = @__(@Inject))
public class ExtrairImagemUserCase {

    SalvarFramesTemporario salvarFrames;

    private static final int FRAME_SKIP = 10;

    public int processarFrames(
            FFmpegFrameGrabber grabber,
            Java2DFrameConverter converter,
            Path outputDiretorio)
            throws IOException {

        int frameNumber = 0;
        int quantidadesFramesSalvos = 0;
        Frame frame;

        criarDiretorioSeNecessario(outputDiretorio);

        while ((frame = grabber.grabImage()) != null) {
            frameNumber++;
            if (frameNumber % FRAME_SKIP != 0)
                continue;
            BufferedImage image = converter.convert(frame);
            if (image != null) {
                salvarFrames.salvarFrame(image, outputDiretorio.toString(), frameNumber);
                quantidadesFramesSalvos++;
            }
        }

        return quantidadesFramesSalvos;

    }

    private void criarDiretorioSeNecessario(Path diretorio) throws IOException {
        if (!Files.exists(diretorio)) {
            Files.createDirectories(diretorio);
            log.log(Level.INFO, "📁 Diretório de saída criado: {0}", diretorio);
        }
    }

}
