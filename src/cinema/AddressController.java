package cinema;

import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@RestController
public class AddressController {
    ConcurrentMap<String, String> addressBook = new ConcurrentHashMap<>();

    @GetMapping("/addresses")
    public ConcurrentMap<String, String> getAddressBook() {
        return addressBook;
    }

    @PostMapping("/addresses")
    public void postAddress(@RequestParam String name, @RequestParam String address) {
        addressBook.put(name, address);
    }

    @DeleteMapping("/addresses")
    public String deleteAddress(@RequestParam String name) {
        addressBook.remove(name);
        return "Address removed.";
    }
}
