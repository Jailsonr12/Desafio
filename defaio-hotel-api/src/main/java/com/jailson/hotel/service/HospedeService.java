package com.jailson.hotel.service;

import com.jailson.hotel.domain.Hospede;
import com.jailson.hotel.dto.HospedeDTO;
import com.jailson.hotel.repository.HospedeRepository;
import org.springframework.stereotype.Service;

@Service
public class HospedeService {

    private final HospedeRepository hospedeRepository;

    public HospedeService(HospedeRepository hospedeRepository) {
        this.hospedeRepository = hospedeRepository;
    }

    public String createGuest(HospedeDTO guest) {
        String validationMessage = validateGuestParameters(guest);


        if (!validationMessage.isEmpty()) {
            return validationMessage;
        }
        String telefone = guest.getTelefone().replaceAll("\\D", "");
        String formataTelefone = telefone.substring(0, 4) + "-" + telefone.substring(4);
        guest.setTelefone(formataTelefone);


        return save(guest);
    }

    private String validateGuestParameters(HospedeDTO guest){
        String documento = guest.getDocumento().replaceAll("\\D", "");
        String telefone = guest.getTelefone().replaceAll("\\D", "");

        if (documento.length() < 12) {
            return "Documento deve ter 12 dígitos numéricos.";
        }

        if (telefone.length() < 8 ) {
            return "Telefone deve ter  8 dígitos numéricos";
        }
        if (hospedeRepository.findByDocumento(documento).isPresent()){
            return "Já existe um hospodete cadastrado com esse documento:" + guest.getDocumento();
        }

        return "";

    }
    private String save(HospedeDTO guest) {
        Hospede entity = new Hospede();
        entity.setNome(guest.getNome());
        entity.setDocumento(guest.getDocumento());
        entity.setTelefone(guest.getTelefone());

        hospedeRepository.save(entity);
        return "Hóspede cadastrado com sucesso!";
    }

}
