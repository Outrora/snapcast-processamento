package br.com.snapcast.exception;

import java.util.logging.Level;

import br.com.snapcast.domain.entities.VideoEvento;
import lombok.extern.java.Log;

@Log
public class ErroAoProcessarArquivo extends RuntimeException {

    private static final String MENSAGEM = "❌ Erro ao processar o arquivo";

    public ErroAoProcessarArquivo() {
        super(MENSAGEM);
    }

    public ErroAoProcessarArquivo(VideoEvento evento, Throwable throwable) {
        super("Erro ao processar o arquivo %s".formatted(evento.nomeDoVideoComExtensao()), throwable);
        log.log(Level.SEVERE, "❌ Erro ao cadastrar arquivo {}", throwable.getMessage());

    }

}
