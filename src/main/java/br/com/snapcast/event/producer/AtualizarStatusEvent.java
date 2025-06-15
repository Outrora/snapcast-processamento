package br.com.snapcast.event.producer;

import br.com.snapcast.domain.entities.StatusVideo;

public interface AtualizarStatusEvent {

    void enviarStatusVideo(StatusVideo video);
}