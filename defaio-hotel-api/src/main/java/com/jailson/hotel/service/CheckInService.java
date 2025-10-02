package com.jailson.hotel.service;

import com.jailson.hotel.domain.CheckIn;
import com.jailson.hotel.domain.Hospede;
import com.jailson.hotel.dto.CheckInDTO;
import com.jailson.hotel.repository.CheckInRepository;
import com.jailson.hotel.repository.HospedeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final HospedeRepository hospedeRepository;

    public CheckInService(CheckInRepository checkInRepository,
                          HospedeRepository hospedeRepository) {
        this.hospedeRepository = hospedeRepository;
        this.checkInRepository = checkInRepository;
    }

    @Transactional
    public String createCheckIn(CheckInDTO checkInDTO) {
        String validationMessage = validateCheckInParameters(checkInDTO);
        if (!validationMessage.isEmpty()) {
            return validationMessage;
        }

        CheckIn checkIn = new CheckIn();
        checkIn.setHospede(checkInDTO.getHospede());
        checkIn.setDataEntrada(checkInDTO.getDataEntrada());
        checkIn.setDataSaida(checkInDTO.getDataSaida());

        checkInRepository.save(checkIn);
        return "Check-in criado com sucesso!";

    }

    private String validateCheckInParameters(CheckInDTO checkInDTO) {
        Long hospedeId = checkInDTO.getHospede().getId();
        Optional<Hospede> hospedeOpt = hospedeRepository.findById(hospedeId);

        if (hospedeOpt.isEmpty()) {
            return "Hóspede não encontrado para o ID " + hospedeId;
        }

        if (checkInDTO == null) {
            return "Erro: objeto CheckIn inválido";
        }
        if (checkInDTO.getHospede() == null || checkInDTO.getHospede().getId() == null) {
            return "Hóspede é obrigatório";
        }
        if (checkInDTO.getDataEntrada() == null) {
            return "Data de entrada é obrigatória";
        }

        if (checkInDTO.getDataSaida() != null && checkInDTO.getDataSaida().isBefore(checkInDTO.getDataEntrada())) {
            return "Data de saída não pode ser anterior à data de entrada";
        }

        return "";
    }

    @Transactional
    public Map<String, Object> readCheckIn(Long id) {
        List<CheckIn> rows = checkInRepository.selectCheckIn(id);
        CheckIn c = rows.isEmpty() ? null : rows.get(0);
        if (c == null) return null;
        Map<String, Object> map = new LinkedHashMap<>(5);
        map.put("id", c.getId());
        map.put("Nome", c.getHospede() != null ? c.getHospede().getNome() : null);
        map.put("Documento", c.getHospede() != null ? c.getHospede().getDocumento() : null);
        map.put("dataEntrada", c.getDataEntrada());
        map.put("dataSaida", c.getDataSaida());
        return map;
    }




    public List<Map<String, Object>> listCheckIn() {
        List<CheckIn> entities = checkInRepository.findAll();
        entities.sort(Comparator.comparing(CheckIn::getId));

        List<Map<String, Object>> list = new ArrayList<>(entities.size());
        for (CheckIn h : entities) {
            Map<String, Object> map = new LinkedHashMap<>(5);
            map.put("id", h.getId());
            map.put("Nome", h.getHospede().getNome());
            map.put("Documento", h.getHospede().getDocumento());
            map.put("dataEntrada", h.getDataEntrada());
            map.put("dataSaida", h.getDataSaida());
            list.add(map);
        }
        return list;
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

    @Transactional
    public String updateCheckIn(Long id, CheckInDTO checkInDTO) {
        int updated = checkInRepository.updateCheckIn(id,
                checkInDTO.isAdicionalVeiculo(),
                checkInDTO.getDataEntrada(),
                checkInDTO.getDataSaida()
        );

        if (updated == 0) {
            return "Check-in não encontrado para o ID: " + id;
        }
        return "Check-in atualizado com sucesso!";
    }

    public String deleteCheckIn(Long id) {
        checkInRepository.deleteById(id);
        return "Check-in deletado com sucesso!";
    }
}
