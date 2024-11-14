package org.example.component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.service.GuestCartService;
import org.example.service.GuestFavoriteService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
@RequiredArgsConstructor
public class GuestCleanTask {

    GuestCartService guestCartService;
    GuestFavoriteService guestFavoriteService;

    @Scheduled(fixedRate = 86400000)
    public void cleanUpGuestData() {
        LocalDateTime expirationTime = LocalDateTime.now().minusDays(1);
        guestCartService.cleanUpIfOld(expirationTime);
        guestFavoriteService.cleanUpIfOld(expirationTime);
        System.out.println("Cleanup completed.");
    }
}