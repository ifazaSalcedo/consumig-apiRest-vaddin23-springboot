package com.apirest.restservices;

import com.apirest.restdata.AutorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;



import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@SuppressWarnings("serial")
@Service
public class AutorRestService implements Serializable {
    private WebClient webClient;

    public AutorRestService(@Autowired WebClient webClient) {
        this.webClient = webClient;
    }

    public List<AutorDTO> findAll(){
        //ENVIAR AUTENTICACION A LA APIREST
        List<AutorDTO> autores = webClient
                .get()
                .uri("/api/v1/biblioteca/autores")
                .retrieve()
                .toEntityList(AutorDTO.class)
                .block()
                .getBody();
        return autores;

    }
    public AutorDTO save(AutorDTO autorDTO){
        AutorDTO autor = webClient
                .post()
                .uri("/api/v1/biblioteca/autores")
                .body(Mono.just(autorDTO), AutorDTO.class)
                .retrieve()
                .bodyToMono(AutorDTO.class)
                .block();
        return autor;
    }
}
