package br.com.snapcast.domain.user_case;

import java.util.List;
import java.util.logging.Level;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import br.com.snapcast.config.Configuracoes;
import br.com.snapcast.domain.entities.StatusVideo;
import br.com.snapcast.domain.entities.VideoEvento;
import br.com.snapcast.port.BaixarArquivo;
import br.com.snapcast.port.EnviarArquivo;
import br.com.snapcast.event.producer.AtualizarStatusEvent;
import br.com.snapcast.exception.ErroAoProcessarArquivo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@ApplicationScoped
@Log
@AllArgsConstructor(onConstructor = @__(@Inject))
public class ProcessarVideoUserCase {

    // User Case
    private ExtrairImagemUserCase extrairImagem;
    private ZiparArquivoUserCase ziparArquivo;

    // Ports
    private BaixarArquivo baixarArquivo;
    private EnviarArquivo enviarArquivo;

    // Event
    private AtualizarStatusEvent atualizarStatus;

    private Configuracoes configuracoes;

    public void processarArquivo(VideoEvento evento) throws Exception {
        log.log(Level.INFO, "🎥 Iniciando processamento do vídeo: {0}", evento.nomeDoVideo());
        Path pathTemporario = Paths.get(configuracoes.getDiretorioVideos()).resolve(evento.nomeDoVideo());

        // Baixado Arquivo do S3
        String videoTemporario = baixarArquivo.baixarArquivo(evento);

        // Separando Os Frames
        Integer quantidadeFramesSalvo = separarFramesVideo(videoTemporario, pathTemporario);

        // Zipando os Frames
        String pathZip = ziparArquivo.ziparFrames(pathTemporario.toString(), evento.nomeDoVideo());

        // Enviando Arquivo para S3
        enviarArquivo.enviarArquivo(pathZip, evento.nomeDoVideo());

        // Deletar arquivos Temporários
        deletandoArquivosTemporarios(List.of(pathZip, videoTemporario));

        // Atualizar que processamento foi concluído
        atualizarStatus.enviarStatusVideo(StatusVideo.videoProcessado(evento.id(), quantidadeFramesSalvo));

        log.log(Level.INFO, "🎥 Finalizado processamento do vídeo: {0}", evento.nomeDoVideo());
    }

    private int separarFramesVideo(String inputDiretorio, Path pathTemporario) throws IOException {
        try (FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(inputDiretorio);
                Java2DFrameConverter converter = new Java2DFrameConverter()) {

            grabber.start();
            return extrairImagem.processarFrames(grabber, converter, pathTemporario);

        } catch (Exception e) {
            log.log(Level.SEVERE, "❌ Erro ao processar vídeo: " + e.getMessage(), e);
            throw new ErroAoProcessarArquivo();
        }
    }

    private void deletandoArquivosTemporarios(List<String> arquivos) {
        arquivos.forEach(x -> {
            try {
                var path = Path.of(x);
                Files.deleteIfExists(path);
            } catch (IOException e) {
                log.log(Level.SEVERE, "❌ Erro ao deletar arquivo:" + x);
            }
        });

    }

}
