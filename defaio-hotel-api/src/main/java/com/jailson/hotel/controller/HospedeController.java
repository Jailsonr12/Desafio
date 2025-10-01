package com.jailson.hotel.controller;

import com.jailson.hotel.dto.HospedeDTO;
import com.jailson.hotel.service.HospedeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hospede") // <-- CORRETO AGORA
public class HospedeController {
    private HospedeService hospedeService;

    public HospedeController(HospedeService hospedeService){
        this.hospedeService = hospedeService;
    }

    @PostMapping("/create")
    public String createGuest(@RequestBody HospedeDTO guest) {
        return hospedeService.createGuest(guest);
    }

    @GetMapping("/ping")
    public String ping() {
        return "API Online! 🚀";
    }
}