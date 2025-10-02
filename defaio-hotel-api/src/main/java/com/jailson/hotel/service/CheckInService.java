package com.jailson.hotel.service;

import com.jailson.hotel.domain.CheckIn;
import com.jailson.hotel.domain.Hospede;
import com.jailson.hotel.dto.CheckInDTO;
import com.jailson.hotel.repository.CheckInRepository;
import com.jailson.hotel.repository.HospedeRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

}
