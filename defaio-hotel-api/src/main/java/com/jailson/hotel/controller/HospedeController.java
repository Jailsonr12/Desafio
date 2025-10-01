package com.jailson.hotel.controller;

import com.jailson.hotel.dto.HospedeDTO;
import com.jailson.hotel.service.HospedeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jdk.jfr.EventType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hospede")
public class HospedeController {
    private HospedeService hospedeService;

    public HospedeController(HospedeService hospedeService){
        this.hospedeService = hospedeService;
    }

    @PostMapping("/create")
    public String createGuest(@Valid @RequestBody HospedeDTO guest) {
        return hospedeService.createGuest(guest);
    }

    @PostMapping("/read")
    public String readGuest(@RequestBody HospedeDTO guest){
        return "hospedeService.readGuest(guest)";
    }

    @Transactional
    @PutMapping("/update/{id}")
    public String updateGuest(@PathVariable("id") Long id, @RequestBody HospedeDTO guest){

        if (guest.getId() != null && !guest.getId().equals(id)) {
            return ResponseEntity.badRequest().body("ID no corpo da requisição não deve ser diferente do ID na URL.").toString();
        }
        guest.setId(id);

        return hospedeService.updateGuest(guest);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteGuest(@PathVariable("id") Long id){
        return hospedeService.deleteGuest(id);
    }

    @GetMapping("/list")
    public List<HospedeDTO> listGuest() {
        return hospedeService.listGuest();
    }


    @GetMapping("/ping")
    public String ping() {
        return "API Online! 🚀";
    }
}