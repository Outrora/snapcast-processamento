package br.com.snapcast.domain.user_case;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import br.com.snapcast.config.Configuracoes;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@ApplicationScoped
@Log
@AllArgsConstructor(onConstructor = @__(@Inject))
public class ZiparArquivoUserCase {

    private static final String ZIP_EXTENSION = ".zip";
    private Configuracoes configuracoes;

    public synchronized String ziparFrames(String caminho, String nome) {

        Path inpuPath = Path.of(caminho);
        String arquivoTemporario = Paths.get(configuracoes.getDiretorioVideos()).resolve(nome).toString()
                + ZIP_EXTENSION;

        log.info("📦 Compactando para ZIP...");
        try (FileOutputStream fos = new FileOutputStream(arquivoTemporario);
                ZipOutputStream zos = new ZipOutputStream(fos)) {

            Files.walk(inpuPath)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile())) {
                            ZipEntry zipEntry = new ZipEntry(path.getFileName().toString());
                            zos.putNextEntry(zipEntry);

                            byte[] buffer = new byte[1024];
                            int len;
                            while ((len = fis.read(buffer)) > 0) {
                                zos.write(buffer, 0, len);
                            }
                            zos.closeEntry();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("🗑️ Excluindo arquivos temporários...");

        try {
            Files.walk(inpuPath)
                    .filter(Files::isRegularFile)
                    .forEach(path -> {
                        try {
                            Files.delete(path);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            Files.deleteIfExists(inpuPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("✅ Processo concluído. 📦 Arquivo  ZIP criado: " + arquivoTemporario);
        return arquivoTemporario;
    }

}
