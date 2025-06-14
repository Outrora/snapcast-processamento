package br.com.snapcast.domain.port;

import br.com.snapcast.domain.entities.VideoEvento;

public interface BaixarArquivo {

    public String baixarArquivo(VideoEvento video);

}
