package com.mealmaster.mealmasterfinal.repository;

import com.mealmaster.mealmasterfinal.model.CalendarSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalendarSlotRepository extends JpaRepository<CalendarSlot, Long> {
    List<CalendarSlot> findByUsername(String username);
    void deleteByUsername(String username);
    
}
