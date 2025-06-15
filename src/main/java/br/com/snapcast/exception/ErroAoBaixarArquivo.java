package br.com.snapcast.exception;

import java.util.logging.Level;

import br.com.snapcast.domain.entities.VideoEvento;
import lombok.extern.java.Log;

@Log
public class ErroAoBaixarArquivo extends RuntimeException {

    public ErroAoBaixarArquivo() {
        super("❌ Erro ao baixar arquivo do S3");
    }

    public ErroAoBaixarArquivo(VideoEvento evento, Throwable throwable) {
        super("Erro ao baixar arquivo do S3 : %s".formatted(evento.nomeDoVideoComExtensao()), throwable);
        log.log(Level.SEVERE, "❌ Erro ao baixar arquivo do S3 : {}", throwable.getMessage());

    }

}
