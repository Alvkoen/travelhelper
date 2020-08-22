package com.alvcohen.travelhelper

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String?): Optional<User>
    fun existsByEmail(email: String?): Boolean
}

@Repository
interface TodoItemRepository : JpaRepository<ToDoItem, Long>

@Repository
interface VisaUsageRepository : JpaRepository<VisaUsage, Long>
