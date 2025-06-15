package br.com.snapcast.domain.entities;

public record VideoEvento(
        String id,
        String nome,
        String formatoVideo,
        Long tamanhoVideo,
        String caminhoVideo) {

    public String nomeDoVideo() {
        int indexPonto = id.lastIndexOf('.');
        return indexPonto > 0 ? id.substring(0, indexPonto) : id;
    }

    public String nomeDoVideoComExtensao() {
        return nomeDoVideo() + "." + formatoVideo;
    }

}
