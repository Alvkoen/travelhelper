package com.alvcohen.travelhelper.service;

import com.alvcohen.travelhelper.dto.VisaUsageAddRequest;
import com.alvcohen.travelhelper.model.AuthProvider;
import com.alvcohen.travelhelper.model.User;
import com.alvcohen.travelhelper.model.VisaUsage;
import com.alvcohen.travelhelper.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        final User user = new User("testuser@example.com", "TestUser", AuthProvider.LOCAL);
        userRepository.save(user);
    }

    @Test
    public void addVisaUsage_addsUsageWithDaysThatIncludeArrival() {
        userService.addVisaUsage("testuser@example.com", new VisaUsageAddRequest("01/02/2020", "07/02/2020"));
        final User updatedUser = userService.getCurrentUser("testuser@example.com");
        assertFalse(updatedUser.getVisaUsages().isEmpty());
        final VisaUsage usage = updatedUser.getVisaUsages().iterator().next();
        assertEquals(1, usage.getArrival().getDayOfMonth());
        assertEquals(2, usage.getArrival().getMonthValue());
        assertEquals(7, usage.getDeparture().getDayOfMonth());
        assertEquals(2, usage.getDeparture().getMonthValue());
        assertEquals(7, usage.getDays());
    }

    @Test
    public void deleteVisaUsageById_cascadeDeletesUsageFromUser() {
        userService.addVisaUsage("testuser@example.com", new VisaUsageAddRequest("01/02/2020", "07/02/2020"));
        final User updatedUser = userService.getCurrentUser("testuser@example.com");
        assertEquals(1, updatedUser.getVisaUsages().size());
        final VisaUsage usage = updatedUser.getVisaUsages().iterator().next();
        userService.deleteVisaUsageById(usage.getId());
        final User userWithDeletedUsage = userService.getCurrentUser("testuser@example.com");
        assertTrue(userWithDeletedUsage.getVisaUsages().isEmpty());
    }
}
