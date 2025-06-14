package br.com.snapcast.exception;

public class ErroAoProcessarArquivo extends RuntimeException {

    public ErroAoProcessarArquivo() {
        super("Erro ao Processar o Arquivo");
    }

}
