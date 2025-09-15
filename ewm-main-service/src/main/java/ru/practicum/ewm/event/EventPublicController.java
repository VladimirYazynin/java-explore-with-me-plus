package ru.practicum.ewm.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.event.dto.EventDto;
import ru.practicum.ewm.event.exceptions.EventNotFound;
import ru.practicum.ewm.event.interfaces.EventService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventPublicController {

    private final EventService eventService;

    @GetMapping
    List<EventDto> getAllPublishedEvents(){
        return List.of();
    }

    @GetMapping("/{id}")
    EventDto getEventById(@PathVariable long id) throws EventNotFound {
        return eventService.getEventById(id);
    }

    @ExceptionHandler(EventNotFound.class)
    ResponseEntity<Map<String,String>> onEventNotFound(EventNotFound exception){
        Map<String, String> body = new HashMap<>();
        body.put("message", exception.getMessage());
        log.info("Item not found: {}", body);
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

}