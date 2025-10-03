package com.jailson.hotel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(
        name = "CheckInUpdatePayload",
        description = "Payload para atualizar check-in (sem 'hospede')",
        example = """
  {
    "dataEntrada": "2025-10-03T08:00:00",
    "dataSaida": "2025-10-04T11:00:00",
    "adicionalVeiculo": true
  }
  """
)
public class CheckInUpdateDTO {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-10-03T08:00:00")
    private LocalDateTime dataEntrada;

    @Schema(example = "2025-10-04T11:00:00")
    private LocalDateTime dataSaida;

    @Schema(example = "true")
    private boolean adicionalVeiculo;

    public LocalDateTime getDataEntrada() { return dataEntrada; }
    public void setDataEntrada(LocalDateTime dataEntrada) { this.dataEntrada = dataEntrada; }

    public LocalDateTime getDataSaida() { return dataSaida; }
    public void setDataSaida(LocalDateTime dataSaida) { this.dataSaida = dataSaida; }

    public boolean isAdicionalVeiculo() { return adicionalVeiculo; }
    public void setAdicionalVeiculo(boolean adicionalVeiculo) { this.adicionalVeiculo = adicionalVeiculo; }
}
