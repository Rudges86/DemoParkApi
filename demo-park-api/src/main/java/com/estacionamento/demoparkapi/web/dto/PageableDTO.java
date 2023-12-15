package com.estacionamento.demoparkapi.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PageableDTO {
    //tem que ser este nome pois o retorno do objeto do tipo Pageable é um content por isso tem que ter o mesmo nome, para quando passarmos no modelmaper pegar tudo
    private List content = new ArrayList<>();
    private boolean first;
    private boolean last;
    //mudando no documento json o number para page
    @JsonProperty("page")
    private int number;
    private int size;
    @JsonProperty("pageElements")
    private int numberOfElements;
    private int totalPages;
    private int totalElements;
}
