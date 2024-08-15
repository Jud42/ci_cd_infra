package com.example.boat.model;

import com.example.boat.dao.BoatRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class BoatInitializer implements CommandLineRunner {

    private final BoatRepository boatRepository;

    public BoatInitializer(BoatRepository boatRepository) {
        this.boatRepository = boatRepository;
    }

    @Override
    public void run(String... args) {
        Boat boat1 = new Boat("Boat 1", "Description boat_1");
        Boat boat2 = new Boat("Boat 2", "Description boat_2");
        Boat boat3 = new Boat("Boat 3", "Description boat_3");

        boatRepository.saveAll(Arrays.asList(boat1, boat2, boat3));
    }
}

