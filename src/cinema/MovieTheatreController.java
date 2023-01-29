package cinema;

import cinema.service.MovieTheatreService;
import cinema.model.Room;
import cinema.model.Seat;
import cinema.model.Token;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MovieTheatreController {
    MovieTheatreService movieTheatreService;

    public MovieTheatreController(MovieTheatreService movieTheatreService) {
        this.movieTheatreService = movieTheatreService;
    }

    @GetMapping("/seats")
    public Room getSeats() {
        return movieTheatreService.getSeats();
    }

    @PostMapping("/purchase")
    public ResponseEntity<ObjectNode> purchaseTicket(@RequestBody Seat seat) {
        ObjectNode response = movieTheatreService.purchaseTicket(seat);

        if (response.has("error")) {
            return ResponseEntity.badRequest().body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/return")
    public ResponseEntity<ObjectNode> refundTicket(@RequestBody final Token token) {
        final ObjectNode response = movieTheatreService.refundTicket(token);

        if (response.has("error")) {
            return ResponseEntity.badRequest().body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/stats")
    public ResponseEntity<ObjectNode> getStats(@RequestParam(required = false) final String password) {
        final ObjectNode response = movieTheatreService.getStats(password);

        if (response.has("error")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } else {
            return ResponseEntity.ok(response);
        }

    }
}