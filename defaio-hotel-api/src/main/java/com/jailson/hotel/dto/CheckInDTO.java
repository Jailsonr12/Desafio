package com.jailson.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jailson.hotel.domain.CheckIn;
import com.jailson.hotel.domain.Hospede;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Schema(
        name = "CheckInPayload",
        description = "Payload para criar/atualizar check-in",
        example = """
    {
      "hospede": { "id": 1 },
      "dataEntrada": "2025-10-03T08:00:00",
      "dataSaida": "2025-10-04T11:00:00",
      "adicionalVeiculo": true
    }
    """
)


public class CheckInDTO {

    @Schema(description = "Referência ao hóspede", example = "{\"id\":1}")
    private Hospede hospede;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-10-03T08:00:00")
    private LocalDateTime dataEntrada;

    @Schema(example = "2025-10-04T11:00:00")
    private LocalDateTime dataSaida;

    @Schema(example = "true", description = "Se precisa de garagem")
    private boolean adicionalVeiculo = false;


    public CheckInDTO(){}

    public  CheckInDTO(CheckIn ententy){
        hospede = ententy.getHospede();
        dataEntrada = ententy.getDataEntrada();
        dataSaida = ententy.getDataSaida();
        adicionalVeiculo = ententy.isAdicionalVeiculo();
    }

    public Hospede getHospede() {
        return hospede;
    }

    public void setHospede(Hospede hospede) {
        this.hospede = hospede;
    }

    public LocalDateTime getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(LocalDateTime dataEntrada) {

        this.dataEntrada = dataEntrada;
    }

    public LocalDateTime getDataSaida() {

        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public boolean isAdicionalVeiculo() {
        return adicionalVeiculo;
    }

    public void setAdicionalVeiculo(boolean adicionalVeiculo) {
        this.adicionalVeiculo = adicionalVeiculo;
    }
}
