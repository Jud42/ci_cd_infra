package com.example.boat.service;

import com.example.boat.dao.BoatRepository;
import com.example.boat.model.Boat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BoatService {

    @Autowired
    private BoatRepository boatRepository;

    public Boat createBoat(Boat boat) {
        // validation attributes requires
        if (boat.getName() == null || boat.getName().isEmpty()) {
            throw new IllegalArgumentException("Le nom du bateau est requis.");
        }
        return boatRepository.save(boat);
    }

    public String getBoatById(Long id) {
        Boat boat = boatRepository.findById(id).orElse(null);
        if (boat != null && boat.getName() != null)
            return boat.getDescription();
        else
            return null;
    }

    public List<Boat> getBoats() {

        /*List<Boat> boats = boatRepository.findAll();

        List<String> boatNames = new ArrayList<>();

        for (Boat boat : boats) {
            boatNames.add(boat.getName());
        }*/

        return boatRepository.findAll();
    }

    public Boolean updateBoat(Boat new_boat) {
        Boat oldboat;
        Optional<Boat> optboat=boatRepository.findById(new_boat.getId());
        if (optboat.isPresent()) {
            oldboat=optboat.get();
            oldboat.setName(new_boat.getName());
            oldboat.setDescription(new_boat.getDescription());
            boatRepository.save(oldboat);
            return true;
        }
        return false;
    }

    public String deleteBoatById(Long id) {
        System.out.print("Hello deleteboat");
        boatRepository.deleteById(id);
        return "Boat deleted";
    }

    public String clearRepo() {
        if (boatRepository.count() == 0)
            return "Empty list";
        boatRepository.deleteAll();
        return "list deleted";
    }
}
