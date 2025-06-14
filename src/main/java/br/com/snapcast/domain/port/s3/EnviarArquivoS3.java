package br.com.snapcast.domain.port.s3;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;

@ApplicationScoped
@AllArgsConstructor(onConstructor = @__(@Inject))
public class EnviarArquivoS3 {

    ClienteS3 s3;
}
