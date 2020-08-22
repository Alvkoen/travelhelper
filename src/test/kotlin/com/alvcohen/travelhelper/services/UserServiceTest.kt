package com.alvcohen.travelhelper.services

import com.alvcohen.travelhelper.AuthProvider
import com.alvcohen.travelhelper.User
import com.alvcohen.travelhelper.UserRepository
import com.alvcohen.travelhelper.VisaUsage
import com.alvcohen.travelhelper.controllers.VisaUsageAddRequest
import junit.framework.Assert.assertEquals
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        val user = User("testuser@example.com", "TestUser", AuthProvider.LOCAL)
        userRepository.save(user)
    }

    @Test
    fun addVisaUsage_addsUsageWithDaysThatIncludeArrival() {
        userService.addVisaUsage("testuser@example.com", VisaUsageAddRequest("01/02/2020", "07/02/2020"))
        val updatedUser: User? = userService.getCurrentUser("testuser@example.com")
        Assert.assertFalse(updatedUser?.let { updatedUser.visaUsages.isEmpty() } ?: false)
        val usage: VisaUsage? = updatedUser?.let{updatedUser.visaUsages.first()}
        assertEquals(1, usage?.let { usage.arrival.dayOfMonth })
        assertEquals(2, usage?.let { usage.arrival.monthValue })
        assertEquals(7, usage?.let { usage.departure.dayOfMonth })
        assertEquals(2, usage?.let { usage.departure.monthValue })
        assertEquals(7L, usage?.let { usage.days })
    }

    @Test
    fun deleteVisaUsageById_cascadeDeletesUsageFromUser() {
        userService.addVisaUsage("testuser@example.com", VisaUsageAddRequest("01/02/2020", "07/02/2020"))
        val updatedUser: User? = userService.getCurrentUser("testuser@example.com")
        assertEquals(1, updatedUser?.let { updatedUser.visaUsages.size })
        val usage: Long? = updatedUser?.let{updatedUser.visaUsages.first().id}
        userService.deleteVisaUsageById(usage)
        val userWithDeletedUsage: User? = userService.getCurrentUser("testuser@example.com")
        Assert.assertTrue(userWithDeletedUsage?.let { userWithDeletedUsage.visaUsages.isEmpty() } ?: false)
    }
}