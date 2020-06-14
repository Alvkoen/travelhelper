package com.alvcohen.travelhelper.service;

import com.alvcohen.travelhelper.dto.TodoItemAddRequest;
import com.alvcohen.travelhelper.dto.VisaUsageAddRequest;
import com.alvcohen.travelhelper.exception.ResourceNotFoundException;
import com.alvcohen.travelhelper.model.ToDoItem;
import com.alvcohen.travelhelper.model.User;
import com.alvcohen.travelhelper.model.VisaUsage;
import com.alvcohen.travelhelper.repository.TodoItemRepository;
import com.alvcohen.travelhelper.repository.UserRepository;
import com.alvcohen.travelhelper.repository.VisaUsageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
public class UserService {
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final UserRepository userRepository;
    private final VisaUsageRepository visaUsageRepository;
    private final TodoItemRepository todoItemRepository;

    @Autowired
    public UserService(final UserRepository userRepository,
                       final VisaUsageRepository visaUsageRepository,
                       final TodoItemRepository todoItemRepository) {
        this.userRepository = userRepository;
        this.visaUsageRepository = visaUsageRepository;
        this.todoItemRepository = todoItemRepository;
    }

    public User getCurrentUser(final String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", email));
    }

    public void addVisaUsage(final String email, final VisaUsageAddRequest request) {
        final User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "id", email));
        final LocalDate arrival = LocalDate.parse(request.arrival, dtf);
        final LocalDate departure = LocalDate.parse(request.departure, dtf);
        final long betweenIncludingArrival = ChronoUnit.DAYS.between(arrival, departure) + 1;
        user.getVisaUsages().add(new VisaUsage(arrival, departure, betweenIncludingArrival));
        userRepository.save(user);
    }

    public void deleteVisaUsageById(final Long id) {
        visaUsageRepository.deleteById(id);
    }

    public void addTodoItem(final String email, final TodoItemAddRequest request) {
        final User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User", "id", email));
        user.getToDoItems().add(new ToDoItem(request.name, request.description));
        userRepository.save(user);

    }

    public void deleteTodoItemById(final long id) {
        todoItemRepository.deleteById(id);
    }
}
