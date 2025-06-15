package br.com.snapcast.criarObjeto;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import br.com.snapcast.domain.entities.VideoEvento;

public abstract class CriarVideoEvento {

    private static List<String> formas = List.of("mp4", "avi");
    private static Random random = new Random();

    public static VideoEvento criar() {
        return new VideoEvento(UUID.randomUUID().toString(), UUID.randomUUID().toString(),
                formas.get(random.nextInt(formas.size())), random.nextLong(),
                ".....");
    }

}
