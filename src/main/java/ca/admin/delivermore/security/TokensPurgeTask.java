package ca.admin.delivermore.security;

import ca.admin.delivermore.collector.data.service.PasswordResetTokenRepository;
import ca.admin.delivermore.views.login.PasswordReset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

@Service
@Transactional
public class TokensPurgeTask {

    private Logger log = LoggerFactory.getLogger(TokensPurgeTask.class);

    @Autowired
    PasswordResetTokenRepository passwordTokenRepository;

    @Scheduled(cron = "${purge.cron.expression}")
    public void purgeExpired() {
        //TODO: need to see how to make this work as it is NOT being called

        Date now = Date.from(Instant.now());
        log.info("purgeExpired: purging expired tokens for user password resets");

        passwordTokenRepository.deleteAllExpiredSince(now);
    }
}