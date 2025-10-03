package com.jailson.hotel.service;

import com.jailson.hotel.domain.CheckIn;
import com.jailson.hotel.domain.Hospede;
import com.jailson.hotel.dto.CheckInDTO;
import com.jailson.hotel.repository.CheckInRepository;
import com.jailson.hotel.repository.HospedeRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class CheckInService {

    private static final BigDecimal WEEKDAY_DAILY  = new BigDecimal("120.00");
    private static final BigDecimal WEEKEND_DAILY  = new BigDecimal("150.00");
    private static final BigDecimal WEEKDAY_GARAGE = new BigDecimal("15.00");
    private static final BigDecimal WEEKEND_GARAGE = new BigDecimal("20.00");
    private static final LocalTime  EXTRA_CUTOFF   = LocalTime.of(16, 30);

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
        checkIn.setAdicionalVeiculo(checkInDTO.isAdicionalVeiculo());

        checkInRepository.save(checkIn);
        return "Check-in criado com sucesso!";

    }

    private String validateCheckInParameters(CheckInDTO checkInDTO) {
        if (checkInDTO == null) {
            return "Erro: objeto CheckIn inválido";
        }
        if (checkInDTO.getHospede() == null || checkInDTO.getHospede().getId() == null) {
            return "Hóspede é obrigatório";
        }
        if (checkInDTO.getDataEntrada() == null) {
            return "Data de entrada é obrigatória";
        }
        if (checkInDTO.getDataSaida() != null &&
                checkInDTO.getDataSaida().isBefore(checkInDTO.getDataEntrada())) {
            return "Data de saída não pode ser anterior à data de entrada";
        }

        Long hospedeId = checkInDTO.getHospede().getId();
        if (hospedeRepository.findById(hospedeId).isEmpty()) {
            return "Hóspede não encontrado para o ID " + hospedeId;
        }
        return "";
    }

    @Transactional
    public Map<String, Object> readCheckIn(Long id) {
        List<CheckIn> rows = checkInRepository.selectCheckIn(id);
        CheckIn c = rows.isEmpty() ? null : rows.get(0);
        if (c == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Check-in não encontrado");
        }
        Map<String, Object> map = new LinkedHashMap<>(6);
        map.put("id", c.getId());
        map.put("nome", c.getHospede() != null ? c.getHospede().getNome() : null);
        map.put("documento", c.getHospede() != null ? c.getHospede().getDocumento() : null);
        map.put("dataEntrada", c.getDataEntrada());
        map.put("dataSaida", c.getDataSaida());
        map.put("adicionalVeiculo", c.isAdicionalVeiculo());
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

    @Transactional
    public List<Map<String, Object>> searchHospedesByAny(String term) {
        if (term == null || term.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Informe o parâmetro 'term'");
        }

        String trimmedTerm = term.trim();
        List<Hospede> entities = hospedeRepository.searchAny(trimmedTerm);
        if (entities.isEmpty()) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> list = new ArrayList<>(entities.size());
        for (Hospede h : entities) {
            List<CheckIn> checkins = checkInRepository.findByHospedeId(h.getId());
            for (CheckIn c : checkins) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("hospedeId", h.getId());
                m.put("nome", h.getNome());
                m.put("documento", h.getDocumento());
                m.put("telefone", h.getTelefone());
                m.put("checkinId", c.getId());
                m.put("dataEntrada", c.getDataEntrada());
                m.put("dataSaida", c.getDataSaida());
                m.put("adicionalVeiculo", c.isAdicionalVeiculo());
                list.add(m);
            }
        }
        return list;

    }

    public List<Map<String, Object>> listOpenCheckIns() {
        List<CheckIn> open = checkInRepository.findByDataSaidaIsNull();
        if (open.isEmpty()) {
            return Collections.emptyList();
        }

        open.sort(Comparator.comparing(
                CheckIn::getDataEntrada,
                Comparator.nullsLast(Comparator.naturalOrder()))
        );

        Map<Long, Map<String, Object>> byGuest = new LinkedHashMap<>();

        for (CheckIn c : open) {
            Long guestId = c.getHospede().getId();

            Map<String, Object> row = byGuest.computeIfAbsent(guestId, id -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("checkinId",c.getId());
                m.put("hospedeId",c.getHospede().getId());
                m.put("nome",c.getHospede().getNome());
                m.put("documento",c.getHospede().getDocumento());
                m.put("telefone",c.getHospede().getTelefone());
                m.put("dataEntrada",c.getDataEntrada());
                m.put("adicionalVeiculo",c.isAdicionalVeiculo());
                return m;
            });

            LocalDateTime currentEntry = (LocalDateTime) row.get("dataEntrada");
            LocalDateTime newEntry = c.getDataEntrada();
            if (newEntry.isAfter(currentEntry)) {
                row.put("checkinId", c.getId());
                row.put("dataEntrada", c.getDataEntrada());
                row.put("adicionalVeiculo", c.isAdicionalVeiculo());
            }
        }

        for (Map.Entry<Long, Map<String, Object>> e : byGuest.entrySet()) {
            Long guestId = e.getKey();
            Map<String, Object> row = e.getValue();
            fillInGuestTotal(row, guestId);
        }

        List<Map<String, Object>> list = new ArrayList<>(byGuest.values());
        list.sort(Comparator.comparing(m -> String.valueOf(m.get("nome")), String.CASE_INSENSITIVE_ORDER));
        return list;
    }

    private void fillInGuestTotal(Map<String, Object> m, Long hospedeId) {
        List<CheckIn> all = checkInRepository.findByHospedeId(hospedeId);

        BigDecimal total = BigDecimal.ZERO;
        CheckIn last = null;

        for (CheckIn c : all) {
            total = total.add(calculateCheckInCost(c));
            if (last == null || (c.getDataEntrada() != null &&
                    c.getDataEntrada().isAfter(last.getDataEntrada()))) {
                last = c;
            }
        }

        BigDecimal lastAccommodationValue = (last != null) ? calculateCheckInCost(last) : BigDecimal.ZERO;
        m.put("valorTotal", total);
        m.put("valorUltimaHospedagem", lastAccommodationValue);
    }


    private BigDecimal calculateCheckInCost(CheckIn c) {
        if (c == null || c.getDataEntrada() == null) return BigDecimal.ZERO;

        LocalDateTime start = c.getDataEntrada();
        LocalDateTime end   = (c.getDataSaida() != null) ? c.getDataSaida() : LocalDateTime.now();

        if (end.isBefore(start)) return BigDecimal.ZERO;

        boolean hasVehicle = c.isAdicionalVeiculo();
        BigDecimal total = BigDecimal.ZERO;

        LocalDate d = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();

        while (d.isBefore(endDate)) {
            total = total.add(dailyRate(d, hasVehicle));
            d = d.plusDays(1);
        }

        if (!end.toLocalTime().isBefore(EXTRA_CUTOFF)) {
            total = total.add(dailyRate(endDate, hasVehicle));
        }

        return total;
    }

    private BigDecimal dailyRate(LocalDate day, boolean hasVehicle) {
        boolean weekend = day.getDayOfWeek() == DayOfWeek.SATURDAY
                || day.getDayOfWeek() == DayOfWeek.SUNDAY;
        BigDecimal base = weekend ? WEEKEND_DAILY : WEEKDAY_DAILY;
        if (hasVehicle) {
            base = base.add(weekend ? WEEKEND_GARAGE : WEEKDAY_GARAGE);
        }
        return base;
    }


    @Transactional
    public List<Map<String, Object>> listClosedCheckIns() {
        List<CheckIn> all = checkInRepository.findByDataSaidaIsNotNull();
        if (all.isEmpty()) {
            return Collections.emptyList();
        }

        all.sort(Comparator.comparing(
                CheckIn::getDataEntrada,
                Comparator.nullsLast(Comparator.naturalOrder()))
        );

        Map<Long, Map<String, Object>> byGuest = new LinkedHashMap<>();

        for (CheckIn c : all) {
            Long guestId = c.getHospede().getId();

            if (checkInRepository.existsByHospedeIdAndDataSaidaIsNull(guestId)) {
                continue;
            }

            Map<String, Object> row = byGuest.computeIfAbsent(guestId, id -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("checkinId",        c.getId());
                m.put("hospedeId",        c.getHospede().getId());
                m.put("nome",             c.getHospede().getNome());
                m.put("documento",        c.getHospede().getDocumento());
                m.put("telefone",         c.getHospede().getTelefone());
                m.put("dataEntrada",      c.getDataEntrada());
                m.put("dataSaida",        c.getDataSaida());
                m.put("adicionalVeiculo", c.isAdicionalVeiculo());
                return m;
            });

            LocalDateTime currentEntry = (LocalDateTime) row.get("dataEntrada");
            LocalDateTime newEntry     = c.getDataEntrada();
            if (newEntry.isAfter(currentEntry)) {
                row.put("checkinId",        c.getId());
                row.put("dataEntrada",      c.getDataEntrada());
                row.put("dataSaida",        c.getDataSaida());
                row.put("adicionalVeiculo", c.isAdicionalVeiculo());
            }
        }

        for (Map.Entry<Long, Map<String, Object>> e : byGuest.entrySet()) {
            Long guestId = e.getKey();
            Map<String, Object> row = e.getValue();
            fillInGuestTotal(row, guestId);
        }

        List<Map<String, Object>> list = new ArrayList<>(byGuest.values());
        list.sort(Comparator.comparing(m -> String.valueOf(m.get("nome")), String.CASE_INSENSITIVE_ORDER));
        return list;
    }


}
