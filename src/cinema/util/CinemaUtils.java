package cinema.util;

import cinema.model.Room;
import cinema.model.Seat;

public class CinemaUtils {

    // Validate if the requested seat is in the room's row-column range
    public static boolean isValidSeat(Room room, Seat seat) {
        return (seat.getRow() > 0 && seat.getRow() <= room.getTotalRows()) &&
                (seat.getColumn() > 0 && seat.getColumn() <= room.getTotalColumns());
    }

    // Check if the requested ticket was already purchased
    public static boolean isAlreadyPurchased(Room room, Seat seat) {
        return !room.getAvailableSeats().contains(seat);
    }

}
