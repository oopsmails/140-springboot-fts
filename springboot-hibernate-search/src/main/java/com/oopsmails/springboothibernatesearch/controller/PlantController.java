package com.oopsmails.springboothibernatesearch.controller;

import com.oopsmails.springboothibernatesearch.model.Institution;
import com.oopsmails.springboothibernatesearch.model.Plant;
import com.oopsmails.springboothibernatesearch.model.SearchRequestDTO;
import com.oopsmails.springboothibernatesearch.service.PlantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/plant")
public class PlantController {

    private final PlantService plantService;

    public PlantController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping("/search")
    public List<Plant> searchPlants(SearchRequestDTO searchRequestDTO) {

        log.info("Request for plant search received with data : " + searchRequestDTO);

        return plantService.searchPlants(searchRequestDTO.getText(), searchRequestDTO.getFields(), searchRequestDTO.getLimit());
    }

    @GetMapping("/qsearch")
    public Page<Plant> searchPlantsByQuery(SearchRequestDTO searchRequestDTO) {

        log.info("Request for searchPlantsByQuery received with data : " + searchRequestDTO);

        return plantService.searchPlantsByQuery(searchRequestDTO.getText(), 1);
    }

    @GetMapping("/mysearch")
    public List<Institution> searchInstitutionsInMemory(SearchRequestDTO searchRequestDTO) {
        log.info("searchInstitutionsInMemory : " + searchRequestDTO);

        return plantService.searchInstitutionsInMemory(searchRequestDTO);
    }
}
