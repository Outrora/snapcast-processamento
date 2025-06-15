package br.com.snapcast.domain.port.s3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import br.com.snapcast.config.Configuracoes;
import br.com.snapcast.domain.entities.VideoEvento;
import br.com.snapcast.domain.port.BaixarArquivo;
import br.com.snapcast.exception.ErroAoBaixarArquivo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@ApplicationScoped
@Log
@AllArgsConstructor(onConstructor = @__(@Inject))
public class BaixarArquivoS3 implements BaixarArquivo {

    Configuracoes config;
    ClienteS3 s3;

    private ResponseInputStream<GetObjectResponse> baixarArquivoTemp(String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(s3.getBucket())
                .key(key)
                .build();

        return s3.pegarS3().getObject(request);
    }

    @Override
    public String baixarArquivo(VideoEvento video) {
        try {
            var temp = baixarArquivoTemp(video.nomeDoVideoComExtensao());

            Path targetPath = Path.of(config.getDiretorioVideos(), video.nomeDoVideoComExtensao());
            Files.createDirectories(targetPath.getParent());

            Files.copy(temp, targetPath, StandardCopyOption.REPLACE_EXISTING);

            log.info("📥 Arquivo " + video.nomeDoVideoComExtensao() + " baixado com sucesso para " + targetPath);

            return targetPath.toString();

        } catch (IOException e) {
            throw new ErroAoBaixarArquivo(video, e);
        }

    }

}
