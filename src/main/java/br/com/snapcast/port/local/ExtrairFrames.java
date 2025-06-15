package br.com.snapcast.port.local;

import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import br.com.snapcast.domain.user_case.ExtrairImagemUserCase;
import br.com.snapcast.exception.ErroAoProcessarArquivo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Log
@ApplicationScoped
@AllArgsConstructor(onConstructor = @__(@Inject))
public class ExtrairFrames {

    // User Case
    private ExtrairImagemUserCase extrairImagem;

    public int separarFramesVideo(String inputDiretorio, Path pathTemporario) throws IOException {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputDiretorio);
                Java2DFrameConverter converter = new Java2DFrameConverter()) {

            grabber.start();
            return extrairImagem.processarFrames(grabber, converter, pathTemporario);

        } catch (Exception e) {
            log.log(Level.SEVERE, "❌ Erro ao processar vídeo: " + e.getMessage(), e);
            throw new ErroAoProcessarArquivo();
        }
    }

}
