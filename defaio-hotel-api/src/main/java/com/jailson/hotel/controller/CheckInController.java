package com.jailson.hotel.controller;

import com.jailson.hotel.dto.CheckInDTO;
import com.jailson.hotel.service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public CheckInDTO readCheckIn(@PathVariable Long id) {

    }

    @PutMapping("/update/{id}")
    public String updateCheckIn(@PathVariable Long id, @RequestBody CheckInDTO checkIn) {

    }

    @DeleteMapping("/delete/{id}")
    public String deleteCheckIn(@PathVariable Long id) {

    }

    @GetMapping("/list")
    public List<CheckInDTO> listCheckIn() {

    }
}
