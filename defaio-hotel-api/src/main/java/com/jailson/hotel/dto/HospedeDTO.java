package com.jailson.hotel.dto;

import com.jailson.hotel.domain.Hospede;
import jakarta.validation.constraints.NotBlank;

public class HospedeDTO {

    private Long id;

    private String nome;

    private String documento;

    private String telefone;

    public HospedeDTO() {}

    public HospedeDTO(Hospede ententy){
        id = ententy.getId();
        nome = ententy.getNome();
        documento = ententy.getDocumento();
        telefone = ententy.getTelefone();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
