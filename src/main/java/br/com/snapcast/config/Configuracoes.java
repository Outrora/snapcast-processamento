package br.com.snapcast.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@ApplicationScoped
@Getter
public class Configuracoes {
    @ConfigProperty(name = "app.video.directory.temp", defaultValue = "/tmp/videos/")
    private String diretorioVideos;

    @ConfigProperty(name = "app.buffer.size", defaultValue = "8192")
    private int bufferSize;

}