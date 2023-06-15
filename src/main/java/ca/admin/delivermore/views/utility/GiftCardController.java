package ca.admin.delivermore.views.utility;

import ca.admin.delivermore.collector.data.service.EmailService;
import ca.admin.delivermore.data.entity.GiftCardEntity;
import ca.admin.delivermore.data.entity.GiftCardWordpress;
import ca.admin.delivermore.data.service.GiftCardRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@AnonymousAllowed
@RestController
@RequestMapping("/giftcardcreate")
public class GiftCardController {

    private static final Logger log = LoggerFactory.getLogger(GiftCardController.class);

    private GiftCardRepository giftCardRepository;
    private Environment environment;
    private EmailService emailService;


    public GiftCardController(GiftCardRepository giftCardRepository, Environment environment, EmailService emailService) {
        this.giftCardRepository = giftCardRepository;
        this.environment = environment;
        this.emailService = emailService;
        log.info("Constructor called:");
    }

    @PostMapping
    public ResponseEntity<String> webHookHandler(@RequestBody String rawBody) {
        log.info("webHookHandler - GiftCardCreate: called. GiftCard: rawBody:" + rawBody);

        ObjectMapper objectMapper = new ObjectMapper();

        GiftCardWordpress gcWordpress = new GiftCardWordpress();
        try {
            gcWordpress = objectMapper.readValue(rawBody, GiftCardWordpress.class);
        } catch (JsonProcessingException e) {
            log.error("webHookHandler - GiftCardCreate: failed. Invalid json: rawBody:" + rawBody);
            return new ResponseEntity<String >(rawBody, HttpStatus.BAD_REQUEST);
        }

        String secret = environment.getProperty("DM_GC_SECRET");
        if(gcWordpress.getSecret().equals(secret)){
            log.info("webHookHandler - GiftCardCreate: secret matched");
        }else{
            log.info("webHookHandler - GiftCardCreate: invalid call to giftcardcreate - not processing");
            return new ResponseEntity<String >(rawBody, HttpStatus.UNAUTHORIZED);
        }

        for (int i = 0; i < gcWordpress.getCount(); i++) {
            log.info("webHookHandler - GiftCardCreate: creating purchased gift card " + (i+1));
            GiftCardEntity gc = new GiftCardEntity();
            gc.setCode(gc.getUniqueCode());
            gc.setCustomerName(gcWordpress.getName());
            gc.setAmount(gcWordpress.getAmount());
            gc.setIssued(LocalDate.now());
            gc.setCustomerEmail(gcWordpress.getEmail());
            gc.setAsGift(gcWordpress.hasRecipient());
            if(gc.getAsGift()){
                gc.setGiftName(gcWordpress.getRecipient());
                gc.setGiftEmail(gcWordpress.getRecipientEmail());
                gc.setGiftNote(gcWordpress.getRecipientNote());
            }
            gc.setNotes("Purchase from delivermore.ca");
            giftCardRepository.save(gc);
            log.info("webHookHandler - GiftCardCreate: gift card saved:" + gc);

            //send email to customer and support with amount and balance
            emailService.sendMailWithHtmlBody(gc.getEmailFullAddress(),gc.getEmailSubject(), gc.getEmailBody());

        }


        return new ResponseEntity<String >(rawBody, HttpStatus.OK);
    }



}
