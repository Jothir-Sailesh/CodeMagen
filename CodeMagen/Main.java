import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

class House {
    private String id, location, owner;
    private double price;
    private int bedrooms;

    public House(String id, String location, double price, int bedrooms, String owner) {
        this.id = id;
        this.location = location;
        this.price = price;
        this.bedrooms = bedrooms;
        this.owner = owner;
    }

    public String getId() { return id; }
    public String getLocation() { return location; }
    public double getPrice() { return price; }
    public int getBedrooms() { return bedrooms; }
    public String getOwner() { return owner; }

    @Override
    public String toString() {
        return id + ", " + location + ", " + price + ", " + bedrooms + ", " + owner;
    }
}

class Tenant {
    private String id, name, contact, preferredLocation;

    public Tenant(String id, String name, String contact, String preferredLocation) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.preferredLocation = preferredLocation;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getPreferredLocation() { return preferredLocation; }
}

class RentalAgreement {
    private House house;
    private Tenant tenant;
    private LocalDate startDate, endDate;
    private double deposit;

    public RentalAgreement(House house, Tenant tenant, LocalDate startDate, LocalDate endDate, double deposit) {
        this.house = house;
        this.tenant = tenant;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deposit = deposit;
    }

    @Override
    public String toString() {
        return "Agreement: " + tenant.getName() + " rents " + house.getId() + " from " + startDate + " to " + endDate;
    }
}
    class HouseNotFoundException extends Exception {
        public HouseNotFoundException(String message) {
            super(message);
        }
    }

class HouseRentalManagementSystem {
    private List<House> houses = new ArrayList<>();
    private List<Tenant> tenants = new ArrayList<>();

    public void addHouse(House house) throws IOException {
        houses.add(house);
        saveToFile("houses.txt", house.toString());
    }

    public void addTenant(Tenant tenant) throws IOException {
        tenants.add(tenant);
        saveToFile("tenants.txt", tenant.getId() + ", " + tenant.getName() + ", " + tenant.getContact() + ", " + tenant.getPreferredLocation());
    }

    public List<House> searchHouses(String location, double maxPrice) {
        return houses.stream()
                .filter(h -> h.getLocation().equalsIgnoreCase(location) && h.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    public synchronized void bookHouse(House house, Tenant tenant) throws HouseNotFoundException, IOException {
        if (!houses.contains(house)) throw new HouseNotFoundException("House not available for booking.");

        RentalAgreement agreement = new RentalAgreement(house, tenant, LocalDate.now(), LocalDate.now().plusMonths(12), house.getPrice() * 2);

        
        saveAgreementToFile("agreements.txt", agreement.toString());

        System.out.println("Booking successful! " + agreement);
    }

    private void saveToFile(String filename, String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(data);
            writer.newLine();
        }
    }

    private void saveAgreementToFile(String filename, String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(data);
            writer.newLine();
        }
    }
}




public class Main {
    public static void main(String[] args) {
        try {
            HouseRentalManagementSystem system = new HouseRentalManagementSystem();

            House h1 = new House("H1", "Bangalore", 50000, 3, "Jothir");
            House h2 = new House("H2", "Mumbai", 75000, 2, "Sailesh");
            system.addHouse(h1);
            system.addHouse(h2);

            Tenant t1 = new Tenant("T1", "JS", "9876543210", "Bangalore");
            system.addTenant(t1);

            List<House> foundHouses = system.searchHouses("Bangalore", 60000);
            System.out.println("Found houses: " + foundHouses);

            system.bookHouse(h1, t1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

