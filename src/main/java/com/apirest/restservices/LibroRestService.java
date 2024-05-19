package com.apirest.restservices;

import com.apirest.restdata.AutorDTO;
import com.apirest.restdata.LibroDTO;
import com.apirest.views.libros.LibroAbmView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class LibroRestService {
    private WebClient webClient;

    public LibroRestService(@Autowired WebClient webClient) {
        this.webClient = webClient;
    }
    public List<LibroDTO> findAll(int offset, int limit){
        //ENVIAR AUTENTICACION A LA APIREST
        List<LibroDTO> books = webClient
                .get()
                .uri("/api/v1/biblioteca/libros")
                .retrieve()
                .toEntityList(LibroDTO.class)
                .block()
                .getBody();
        return books;
    }
    public LibroDTO save(LibroDTO libroDTO){
        LibroDTO book = webClient
                .post()
                .uri("/api/v1/biblioteca/libros")
                .body(Mono.just(libroDTO), LibroDTO.class)
                .retrieve()
                .bodyToMono(LibroDTO.class)
                .block();
        return book;
    }
    public void deleteById(LibroDTO libroDTO){
         webClient
                .delete()
                .uri("/api/v1/biblioteca/libros/{id}", libroDTO.getCodigo())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
