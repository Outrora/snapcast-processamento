package br.com.snapcast.domain.user_case;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import br.com.snapcast.domain.port.SalvarFramesTemporario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@ApplicationScoped
@Log
@AllArgsConstructor(onConstructor = @__(@Inject))
public class ExtrairImagemUserCase {

    SalvarFramesTemporario salvarFrames;

    public void processarFrames(FFmpegFrameGrabber grabber, Java2DFrameConverter converter, Path outputDiretorio)
            throws Exception {

        int[] frameNumber = { 0 };
        Frame frame;

        criarDiretorioSeNecessario(outputDiretorio);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        while ((frame = grabber.grabImage()) != null) {
            BufferedImage image = converter.convert(frame);
            if (image != null) {
                CompletableFuture<Void> future = CompletableFuture
                        .runAsync(() -> salvarFrames.salvarFrame(image, outputDiretorio.toString(), frameNumber[0]));
                futures.add(future);
                frameNumber[0]++;
            }
        }

        CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[0]))
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        log.log(Level.SEVERE, "❌ Erro ao processar frames", throwable);
                    } else {
                        log.log(Level.INFO, "✅ Processamento concluído. Total de frames extraídos: {0}",
                                frameNumber[0]);
                    }
                })
                .join();

    }

    private void criarDiretorioSeNecessario(Path diretorio) throws IOException {
        if (!Files.exists(diretorio)) {
            Files.createDirectories(diretorio);
            log.log(Level.INFO, "📁 Diretório de saída criado: {0}", diretorio);
        }
    }

}
