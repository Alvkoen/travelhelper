package com.alvcohen.travelhelper.services

import com.alvcohen.travelhelper.*
import com.alvcohen.travelhelper.controllers.TodoItemAddRequest
import com.alvcohen.travelhelper.controllers.VisaUsageAddRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Service
class UserService(@Autowired val userRepository: UserRepository,
                  @Autowired val visaUsageRepository: VisaUsageRepository,
                  @Autowired val todoItemRepository: TodoItemRepository) {

    private val dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun getCurrentUser(email: String?): User {
        return userRepository.findByEmail(email)
                .orElseThrow { ResourceNotFoundException("User", "id", email) }
    }

    fun addVisaUsage(email: String?, request: VisaUsageAddRequest) {
        val user = userRepository.findByEmail(email).orElseThrow { ResourceNotFoundException("User", "id", email) }
        val arrival = LocalDate.parse(request.arrival, dtf)
        val departure = LocalDate.parse(request.departure, dtf)
        val betweenIncludingArrival = ChronoUnit.DAYS.between(arrival, departure) + 1
        user.visaUsages.add(VisaUsage(null, arrival, departure, betweenIncludingArrival))
        userRepository.save(user)
    }

    fun deleteVisaUsageById(id: Long?) {
        id?.let{visaUsageRepository.deleteById(id)}
    }

    fun addTodoItem(email: String?, request: TodoItemAddRequest) {
        val user = userRepository.findByEmail(email).orElseThrow { ResourceNotFoundException("User", "id", email) }
        user.toDoItems.add(ToDoItem(null, request.name, request.description))
        userRepository.save(user)
    }

    fun deleteTodoItemById(id: Long) {
        todoItemRepository.deleteById(id)
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(resourceName: String?,
                                fieldName: String?,
                                fieldValue: Any?)
    : RuntimeException(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue))