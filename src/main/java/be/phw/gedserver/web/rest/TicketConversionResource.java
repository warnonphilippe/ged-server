package be.phw.gedserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import be.phw.gedserver.domain.TicketConversion;
import be.phw.gedserver.web.rest.util.HeaderUtil;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * REST controller for managing TicketConversion.
 */
@RestController
@RequestMapping("/api")
public class TicketConversionResource {

    private final Logger log = LoggerFactory.getLogger(TicketConversionResource.class);

    private static final String ENTITY_NAME = "gedTicketConversion";

    public TicketConversionResource() {
    }

    /**
     * GET  /ticket-conversions/:id : get the "id" ticketConversion.
     *
     * @param id the id of the ticketConversion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ticketConversion, or with status 404 (Not Found)
     */
    @GetMapping("/ticket-conversions/{id}")
    @Timed
    public ResponseEntity<TicketConversion> getTicketConversion(@PathVariable Long id) {
        log.debug("REST request to get TicketConversion : {}", id);
        TicketConversion t = new TicketConversion();
        t.setId(1L);
        t.setOk(true);
        t.setEndDate(ZonedDateTime.now());
        return ResponseUtil.wrapOrNotFound(Optional.of(t));
    }

    /**
     * DELETE  /ticket-conversions/:id : delete the "id" ticketConversion.
     *
     * @param id the id of the ticketConversion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/ticket-conversions/{id}")
    @Timed
    public ResponseEntity<Void> deleteTicketConversion(@PathVariable Long id) {
        log.debug("REST request to delete TicketConversion : {}", id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
