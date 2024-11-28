package org.alainshop.component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.alainshop.service.GuestCartService;
import org.alainshop.service.GuestFavoritesService;
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
    GuestFavoritesService guestFavoritesService;

    @Scheduled(fixedRate = 86400000)
    public void cleanUpGuestData() {
        LocalDateTime expirationTime = LocalDateTime.now().minusDays(1);
        guestCartService.cleanUpIfOld(expirationTime);
        guestFavoritesService.cleanUpIfOld(expirationTime);
        System.out.println("Cleanup completed.");
    }
}