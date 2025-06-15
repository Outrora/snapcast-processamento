package br.com.snapcast.domain.entities;

public record VideoEvento(
        String id,
        String nome,
        String formatoVideo,
        Long tamanhoVideo,
        String caminhoVideo) {

    public String nomeDoVideo() {
        int indexPonto = nome.lastIndexOf('.');
        return indexPonto > 0 ? nome.substring(0, indexPonto) : nome;
    }

    public String nomeDoVideoComExtensao() {
        return nomeDoVideo() + "." + formatoVideo;
    }

}
