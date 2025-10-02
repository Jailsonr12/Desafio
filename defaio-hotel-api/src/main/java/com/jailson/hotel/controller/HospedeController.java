package com.jailson.hotel.controller;

import com.jailson.hotel.dto.HospedeDTO;
import com.jailson.hotel.service.HospedeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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
    public ResponseEntity<String> createGuest(@Valid @RequestBody HospedeDTO guest) {
        String result = hospedeService.createGuest(guest);
        if (result.startsWith("Erro:") || result.startsWith("Documento") || result.startsWith("Nome") || result.startsWith("Telefone") || result.startsWith("Já existe")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/read")
    public ResponseEntity<List<HospedeDTO>> readGuest(
            @RequestParam(required = false) String documento,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String telefone) {
        List<HospedeDTO> results = hospedeService.readGuest(documento, nome, telefone);
        if (results == null || results.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(results);
    }

    @Transactional
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateGuest(@PathVariable("id") Long id, @Valid @RequestBody HospedeDTO guest){

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


    @GetMapping("/ping")
    public String ping() {
        return "API Online! 🚀";
    }
}