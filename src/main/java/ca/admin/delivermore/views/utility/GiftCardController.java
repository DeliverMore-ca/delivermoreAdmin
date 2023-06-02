package ca.admin.delivermore.views.utility;

import ca.admin.delivermore.data.service.GiftCardRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AnonymousAllowed
@RestController
@RequestMapping("/giftcardcreate")
public class GiftCardController {

    private static final Logger log = LoggerFactory.getLogger(GiftCardController.class);

    private GiftCardRepository giftCardRepository;

    public GiftCardController(GiftCardRepository giftCardRepository) {
        this.giftCardRepository = giftCardRepository;
        log.info("Constructor called:");
    }

    @PostMapping
    public ResponseEntity<String> webHookHandler(@RequestBody String rawBody) {
        log.info("Log - GiftCardCreate: called. GiftCard: rawBody:" + rawBody);
        System.out.println("System - GiftCardCreate: called. GiftCard: rawBody:" + rawBody);
        //return giftCardRepository.save(newGiftCards);
        /*
        GiftCardEntity gc = new GiftCardEntity();
        gc.setCustomerName("test");
        gc.setIssued(LocalDate.now());

         */
        return new ResponseEntity<String >(rawBody, HttpStatus.OK);
    }


}
