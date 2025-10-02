package com.jailson.hotel.controller;

import com.jailson.hotel.dto.HospedeDTO;
import com.jailson.hotel.service.HospedeService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hospede")
public class HospedeController {
    private HospedeService hospedeService;

    public HospedeController(HospedeService hospedeService){
        this.hospedeService = hospedeService;
    }

    @PostMapping("/create")
    public String createGuest( @RequestBody HospedeDTO guest) {
        return  hospedeService.createGuest(guest);
    }


    @GetMapping("/read/{id}")
    public List<HospedeDTO> readGuestById(@PathVariable("id") Long id) {
        return hospedeService.readGuest(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateGuest(@PathVariable("id") Long id,@RequestBody HospedeDTO guest){

        if (guest.getId() != null && !guest.getId().equals(id)) {
            return ResponseEntity.badRequest().body("ID no corpo da requisição não deve ser diferente do ID na URL.");
        }
        guest.setId(id);

        String result = hospedeService.updateGuest(guest);
        if (result.startsWith("Hóspede não encontrado")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteGuest(@PathVariable("id") Long id){
        String result = hospedeService.deleteGuest(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/list")
    public List<Map<String, Object>> listGuest() {
        return hospedeService.listGuest();
    }

    @PostMapping("/search")
    public List<HospedeDTO> search(@RequestBody HospedeDTO guest) {
        var encontrados = hospedeService.search(guest);
        return encontrados.stream().map(HospedeDTO::new).toList();
    }



    @GetMapping("/ping")
    public String ping() {
        return "API Online! 🚀";
    }
}