package br.com.snapcast.domain.port.local;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import br.com.snapcast.domain.entities.VideoEvento;
import br.com.snapcast.domain.port.BaixarArquivo;
import br.com.snapcast.exception.ErroAoProcessarArquivo;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.java.Log;

@Log
public class BaixarArquivoLocal implements BaixarArquivo {

    @ConfigProperty(name = "video.storage.path", defaultValue = "/opt/snapcast/processamento/videos")
    String baseStoragePath;

    @Override
    public String baixarArquivo(VideoEvento video) {

        var arquivo = Path.of(video.caminhoVideo());
        try {
            Path diretorioDestino = Path.of(baseStoragePath);
            Files.createDirectories(diretorioDestino);

            String nomeArquivo = arquivo.getFileName().toString();
            Path destino = diretorioDestino.resolve(nomeArquivo);
            Files.move(arquivo, destino, StandardCopyOption.REPLACE_EXISTING);

            log.info("👌 Arquivo movido com sucesso para: " + destino);
            return destino.toString();

        } catch (IOException e) {
            throw new ErroAoProcessarArquivo();
        }
    }

}
