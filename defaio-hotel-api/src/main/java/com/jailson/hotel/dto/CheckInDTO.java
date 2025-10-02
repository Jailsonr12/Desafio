package com.jailson.hotel.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jailson.hotel.domain.CheckIn;
import com.jailson.hotel.domain.Hospede;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class CheckInDTO {

    private Hospede hospede;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
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
