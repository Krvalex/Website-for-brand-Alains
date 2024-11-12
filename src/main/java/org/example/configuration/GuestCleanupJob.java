package org.example.configuration;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.example.service.GuestCartService;
import org.example.service.GuestFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class GuestCleanupJob {

    GuestCartService guestCartService;
    GuestFavoriteService guestFavoriteService;

    @Autowired
    public GuestCleanupJob(GuestCartService guestCartService, GuestFavoriteService guestFavoriteService) {
        this.guestCartService = guestCartService;
        this.guestFavoriteService = guestFavoriteService;
    }

    @Scheduled(fixedRate = 86400000)
    public void cleanUpOldGuestData() {
        LocalDateTime expirationTime = LocalDateTime.now().minusDays(1);
        guestCartService.cleanUpIfOld(expirationTime);
        guestFavoriteService.cleanUpIfOld(expirationTime);
        System.out.println("Cleanup job completed.");
    }
}