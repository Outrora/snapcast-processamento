package br.com.snapcast.domain.port.s3;

import java.nio.file.Path;

import br.com.snapcast.domain.port.EnviarArquivo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ApplicationScoped
@AllArgsConstructor(onConstructor = @__(@Inject))
@Log
public class EnviarArquivoS3 implements EnviarArquivo {

    ClienteS3 s3;
    private static final String EXTENSAO = ".zip";

    @Override
    public String enviarArquivo(String caminhoArquivo, String nome) {
        Path file = Path.of(caminhoArquivo);
        log.info("🚀 Enviando Arquivo para: S3");
        s3.pegarS3().putObject(
                PutObjectRequest.builder()
                        .bucket(s3.getBucket())
                        .key(nome + EXTENSAO)
                        .contentType("application/octet-stream")
                        .build(),
                file);

        log.info("👌 Arquivo movido com sucesso para S3 ");
        return "https://%s.s3.%s.amazonaws.com/%s".formatted(s3.getBucket(), s3.getRegiao(), nome);

    }

}
