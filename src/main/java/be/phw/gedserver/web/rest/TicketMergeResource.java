package be.phw.gedserver.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import be.phw.gedserver.domain.TicketMerge;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * REST controller for managing TicketMerge.
 */
@RestController
@RequestMapping("/api")
public class TicketMergeResource {

    private final Logger log = LoggerFactory.getLogger(TicketMergeResource.class);

    private static final String ENTITY_NAME = "gedTicketMerge";

    
    public TicketMergeResource() {
    }

    /**
     * GET  /ticket-merges/:id : get the "id" ticketMerge.
     *
     * @param id the id of the ticketMerge to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ticketMerge, or with status 404 (Not Found)
     */
    @GetMapping("/ticket-merges/{id}")
    @Timed
    public ResponseEntity<TicketMerge> getTicketMerge(@PathVariable Long id) {
        log.debug("REST request to get TicketMerge : {}", id);
        TicketMerge t = new TicketMerge();
        t.setId(1L);
        t.setOk(true);
        t.setEndDate(ZonedDateTime.now());
        return ResponseUtil.wrapOrNotFound(Optional.of(t));
    }

}
