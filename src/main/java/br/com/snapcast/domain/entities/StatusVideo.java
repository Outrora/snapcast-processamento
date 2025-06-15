package br.com.snapcast.domain.entities;

public record StatusVideo(String id, StatusProcessamento processamento, Integer quantidadeFrames) {

    public static StatusVideo videoProcessado(String id, int frames) {
        return new StatusVideo(id, StatusProcessamento.CONCLUIDO, frames);
    }

    public static StatusVideo erroAoProcessar(String id) {
        return new StatusVideo(id, StatusProcessamento.FALHA, 0);
    }
}