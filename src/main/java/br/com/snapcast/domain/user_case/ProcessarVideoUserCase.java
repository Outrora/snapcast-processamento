package br.com.snapcast.domain.user_case;

import java.util.logging.Level;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import br.com.snapcast.config.Configuracoes;
import br.com.snapcast.domain.entities.VideoEvento;
import br.com.snapcast.domain.port.BaixarArquivo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@ApplicationScoped
@Log
@AllArgsConstructor(onConstructor = @__(@Inject))
public class ProcessarVideoUserCase {

    private ExtrairImagemUserCase extrairImagem;
    private ZiparArquivoUserCase ziparArquivo;
    private BaixarArquivo baixarArquivo;

    private Configuracoes configuracoes;

    public void processarArquivo(VideoEvento evento) throws Exception {

        String videoTemporario = baixarArquivo.baixarArquivo(evento);
        Path outputDiretorio = Paths.get(configuracoes.getDiretorioVideos()).resolve(evento.nomeDoVideo());

        log.log(Level.INFO, "🎥 Iniciando processamento do vídeo: {0}", evento.nomeDoVideo());
        processarArquivo(videoTemporario, outputDiretorio);
        ziparArquivo.ziparArquivo(outputDiretorio.toString(), evento.nomeDoVideo());

    }

    private void processarArquivo(String inputDiretorio, Path outputDiretorio) throws IOException {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputDiretorio);
                Java2DFrameConverter converter = new Java2DFrameConverter()) {

            grabber.start();
            extrairImagem.processarFrames(grabber, converter, outputDiretorio);

        } catch (Exception e) {
            log.log(Level.SEVERE, "❌ Erro ao processar vídeo: " + e.getMessage(), e);
            throw new IOException("Falha ao processar vídeo", e);
        }
    }

}
