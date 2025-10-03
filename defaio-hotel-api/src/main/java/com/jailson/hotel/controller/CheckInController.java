package com.jailson.hotel.controller;

import com.jailson.hotel.domain.CheckIn;
import com.jailson.hotel.dto.CheckInDTO;
import com.jailson.hotel.dto.CheckInUpdateDTO;
import com.jailson.hotel.dto.HospedeDTO;
import com.jailson.hotel.service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/checkin")
public class CheckInController {

    private final CheckInService checkInService;

    @Autowired
    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @PostMapping("/create")
    public String createCheckIn(@RequestBody CheckInDTO checkIn) {
            return checkInService.createCheckIn(checkIn);
    }

    @GetMapping("/read/{id}")
    public Map<String, Object> readCheckIn(@PathVariable Long id) {
        return checkInService.readCheckIn(id);
    }

    @PutMapping("/update/{id}")
    public String updateCheckIn(@PathVariable Long id, @RequestBody CheckInUpdateDTO checkIn) {
        return checkInService.updateCheckIn(id, checkIn);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCheckIn(@PathVariable Long id) {
        return  checkInService.deleteCheckIn(id);
    }

    @GetMapping("/list")
    public List<Map<String, Object>> listCheckIn() {
        return checkInService.listCheckIn();
    }

    @GetMapping("/search")
    public List<Map<String, Object>> searchHospedes(@RequestParam String term) {
        return checkInService.searchHospedesByAny(term);
    }

    @GetMapping("/open")
    public List<Map<String, Object>> listOpenCheckIns() {
        return checkInService.listOpenCheckIns();
    }

    @GetMapping("/closed")
    public List<Map<String, Object>> listClosedCheckIns() {
        return checkInService.listClosedCheckIns();
    }

}
