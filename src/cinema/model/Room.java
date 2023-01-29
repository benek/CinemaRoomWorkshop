package cinema.model;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private int totalRows;
    private int totalColumns;
    private final List<Seat> availableSeats;

    public Room(int totalRows, int totalColumns) {
        availableSeats = new ArrayList<>();
        this.totalRows = totalRows;
        this.totalColumns = totalColumns;

        for (int row = 1; row <= this.totalRows; row++) {
            for (int column = 1; column <= this.totalColumns; column++) {
                availableSeats.add(new Seat(row, column));
            }
        }
    }

    public List<Seat> getAvailableSeats() {
        return availableSeats;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalColumns() {
        return totalColumns;
    }

    public void setTotalColumns(int totalColumns) {
        this.totalColumns = totalColumns;
    }
}
