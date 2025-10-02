package com.jailson.hotel.service;

import com.jailson.hotel.domain.Hospede;
import com.jailson.hotel.dto.HospedeDTO;
import com.jailson.hotel.repository.HospedeRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.apache.logging.log4j.util.Strings.trimToNull;

@Service
public class HospedeService {

    private final HospedeRepository hospedeRepository;

    public HospedeService(HospedeRepository hospedeRepository) {
        this.hospedeRepository = hospedeRepository;
    }

    public String createGuest(HospedeDTO guest) {
        if (guest == null) {
            return "Erro: objeto hospede inválido";
        }
        String validationMessage = validateGuestParameters(guest);
        if (!validationMessage.isEmpty()) {
            return validationMessage;
        }

        // Normaliza telefone e documento para armazenar
        String telefoneDigits = guest.getTelefone().replaceAll("\\D", "");
        String formataTelefone = telefoneDigits.substring(0, 4) + "-" + telefoneDigits.substring(4);

        String documentoDigits = guest.getDocumento().replaceAll("\\D", "");

        guest.setTelefone(formataTelefone);
        guest.setDocumento(documentoDigits);
        return save(guest);
    }

    private String validateGuestParameters(HospedeDTO guest){
        String documento = guest.getDocumento() == null ? "" : guest.getDocumento().replaceAll("\\D", "");
        String telefone = guest.getTelefone() == null ? "" : guest.getTelefone().replaceAll("\\D", "");
        String nome = guest.getNome() == null ? "" : guest.getNome().trim();

        if (documento.length() != 12) {
            return "Documento deve ter 12 dígitos numéricos.";
        }

        if (nome.isEmpty()) {
            return "Nome não pode ser vazio.";
        }

        if (telefone.length() != 8 ) {
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

    public List<Map<String, Object>> listGuest() {
        List<Hospede> entities = hospedeRepository.findAll();
        entities.sort(Comparator.comparing(Hospede ::getId));
        List<Map<String, Object>> list = new ArrayList<>(entities.size());
        for (Hospede h : entities) {
            Map<String, Object> map = new LinkedHashMap<>(4);
            map.put("id",        h.getId());
            map.put("nome",      h.getNome());
            map.put("documento", h.getDocumento());
            map.put("telefone",  h.getTelefone());
            list.add(map);
        }
        return list;
    }



    public String deleteGuest (Long id){
         hospedeRepository.deleteById(id);
         return "Hóspede deletado com sucesso!";
    }

    @Transactional
    public String updateGuest(HospedeDTO guest){
        int update = hospedeRepository.updateGuest(
                guest.getNome(),
                guest.getDocumento(),
                guest.getTelefone(),
                guest.getId()
        );

        if (update == 0) {
            return "Hóspede não encontrado para o ID: " + guest.getId();
        }
        return "Hóspede atualizado com sucesso!";
    }

    public List<Hospede> readGuest(long id) {
        return hospedeRepository.selectGuestId(id);
    }

    public List<Hospede> search(HospedeDTO guest) {
        if (guest == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Insira algum parâmetro para encontrar a pessoa");
        }

        if (guest.getDocumento() != null && !guest.getDocumento().isBlank()) {
            return hospedeRepository.selectGuestDocumento(guest.getDocumento().trim());
        }

        if (guest.getNome() != null && !guest.getNome().isBlank()) {
            return hospedeRepository.findByNomeContainingIgnoreCase(guest.getNome().trim());
        }

        if (guest.getTelefone() != null && !guest.getTelefone().isBlank()) {
            return hospedeRepository.findByTelefone(guest.getTelefone().trim());
        }

        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                "Insira algum parâmetro para encontrar a pessoa");
    }
}
