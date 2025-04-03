package com.mealmaster.mealmasterfinal.controller;

import com.mealmaster.mealmasterfinal.dto.CalendarSlotDTO;
import com.mealmaster.mealmasterfinal.dto.IngredientDTO;
import com.mealmaster.mealmasterfinal.model.CalendarSlot;
import com.mealmaster.mealmasterfinal.repository.CalendarSlotRepository;
import com.mealmaster.mealmasterfinal.service.ShoppingListService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/calendar")
public class CalendarController {

    @Autowired
    private CalendarSlotRepository calendarSlotRepository;
    @Autowired
    private ShoppingListService shoppingListService;


    @PostMapping("/save")
    @ResponseBody
    @Transactional // ← ajoute cette ligne
    public ResponseEntity<String> savePlanning(@RequestBody List<CalendarSlotDTO> slots, Principal principal) {
        String username = principal.getName();

        calendarSlotRepository.deleteByUsername(username);

        List<CalendarSlot> entities = slots.stream().map(dto -> {
            CalendarSlot slot = new CalendarSlot();
            slot.setCaseId(dto.getCaseId());
            slot.setRecipeName(dto.getRecipeName());
            slot.setUsername(username);
            return slot;
        }).toList();

        calendarSlotRepository.saveAll(entities);
        return ResponseEntity.ok("Planning saved");
    }

    @GetMapping("/load")
    @ResponseBody
    public List<CalendarSlotDTO> loadPlanning(Principal principal) {
        String username = principal.getName();

        return calendarSlotRepository.findByUsername(username).stream()
                .map(slot -> {
                    CalendarSlotDTO dto = new CalendarSlotDTO();
                    dto.setCaseId(slot.getCaseId());
                    dto.setRecipeName(slot.getRecipeName());
                    return dto;
                })
                .toList();
    }

    @PostMapping("/reset")
    @ResponseBody
    @Transactional
    public ResponseEntity<String> resetPlanning(Principal principal) {
        String username = principal.getName();
        calendarSlotRepository.deleteByUsername(username);
        return ResponseEntity.ok("Planning reset");
    }

    @GetMapping("/shopping-list")
    @ResponseBody
    public List<IngredientDTO> getShoppingList(Principal principal) {
        return shoppingListService.getAggregatedIngredients(principal.getName());
    }



}
