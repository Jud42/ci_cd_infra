package com.example.boat.controller;

import com.example.boat.model.Boat;
import com.example.boat.service.BoatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@RestController
@RequestMapping
@Validated
public class BoatController {
    @Autowired
    private BoatService boatService;

    @GetMapping("/")
    public <T> T getAllBoats() {
        List<Boat> boats = boatService.getBoats();
        return (boats.isEmpty()) ? (T) "Empty list" : (T) boats;
    }

    @GetMapping("/getBoat/{id}")
    public String getBoat(@PathVariable Long id) {
        return boatService.getBoatById(id);
    }

    @PostMapping("/addBoat")
    public ResponseEntity<?> createBoat(@Valid @RequestBody Boat boat) {

        // if validation is ok
        boatService.createBoat(boat);
        return ResponseEntity.ok("Boat is create!");
    }
    @PutMapping("/updBoat/{id}")
    public ResponseEntity<?> updateBoat(@Valid @PathVariable Long id, @RequestBody Boat boat) {
        boat.setId(id);
        if (boatService.updateBoat(boat))
            return ResponseEntity.ok("Boat updated!");
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/deleteBoat/{id}")
    public String deleteBoat(@PathVariable Long id) {
        return boatService.deleteBoatById(id);
    }

    @DeleteMapping("/clearList")
    public String deleteBoats() {
        return boatService.clearRepo();
    }
}
