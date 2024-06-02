package com.apirest.restservices;

import com.apirest.restdata.LibroDTO;
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
    public List<LibroDTO> findAll(){
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
    public LibroDTO save(LibroDTO libroDTO) throws RuntimeException{
        LibroDTO book = webClient
                .post()
                .uri("/api/v1/biblioteca/libros")
                .body(Mono.just(libroDTO), LibroDTO.class)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> clientResponse.bodyToMono(String.class).flatMap(errorBody -> {
                            return Mono.error(new RuntimeException("Error: " + errorBody));
                        }))
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
