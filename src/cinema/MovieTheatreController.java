package cinema;

import cinema.logic.CinemaUtils;
import cinema.model.Room;
import cinema.model.Seat;
import cinema.model.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
public class MovieTheatreController {
    private final Room mainRoom = new Room(9, 9);
    private final ObjectMapper mapper = new ObjectMapper();
    private final Map<String, Seat> purchasedSeats = new HashMap<>();
    private Integer income = 0;

    @GetMapping("/seats")
    public Room getSeats() {
        return mainRoom;
    }

    @PostMapping("/purchase")
    public ResponseEntity<ObjectNode> purchaseTicket(@RequestBody Seat seat) {
        final ObjectNode response = mapper.createObjectNode();
        response.set("ticket", mapper.valueToTree(seat));

        if (!CinemaUtils.isValidSeat(mainRoom, seat)){
            response.put("error", "The number of a row or a column is out of bounds!");
            return ResponseEntity.badRequest().body(response);
        }

        if (CinemaUtils.isAlreadyPurchased(mainRoom, seat)){
            response.put("error", "The ticket has been already purchased!");
            return ResponseEntity.badRequest().body(response);
        }

        // Generating a token that can be used to refund the ticket
        String token = UUID.randomUUID().toString();
        purchasedSeats.put(token, seat);
        response.put("token", token);

        // Increasing income
        income += seat.getPrice();

        // Marking seat as purchased removing it from the available seats
        mainRoom.getAvailableSeats().remove(seat);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/return")
    public ResponseEntity<ObjectNode> refundTicket(@RequestBody final Token token) {
        final ObjectNode response = mapper.createObjectNode();
        System.out.println("Received token to refund: " + token.getToken());
        Seat seatToRefund = purchasedSeats.get(token.getToken());

        if (seatToRefund == null) {
            response.put("error", "Wrong token!");
            return ResponseEntity.badRequest().body(response);
        }

        mainRoom.getAvailableSeats().add(seatToRefund);
        purchasedSeats.remove(token.getToken());
        income -= seatToRefund.getPrice();
        response.set("returned_ticket", mapper.valueToTree(seatToRefund));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/stats")
    public ResponseEntity<ObjectNode> getStats(@RequestParam(required = false) final String password) {
        final ObjectNode response = mapper.createObjectNode();

        if (("super_secret").equals(password)) {
            response.put("current_income", income);
            response.put("number_of_available_seats", mainRoom.getAvailableSeats().size());
            response.put("number_of_purchased_tickets", purchasedSeats.size());
        } else {
            response.put("error", "The password is wrong!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        return ResponseEntity.ok(response);
    }
}