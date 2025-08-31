import java.util.*;

//--------------------------------------------------
// 1.Enum 
//--------------------------------------------------
enum TransportMode { TRAIN, CAR, FLIGHT, SHIP }
enum CourierStatus { BOOKED, IN_TRANSIT, DELIVERED }

//--------------------------------------------------
// 2. Custom 
//--------------------------------------------------
class InvalidTransportException extends Exception {
    public InvalidTransportException(String msg) { super(msg); }
}

class InvalidWeightException extends Exception {
    public InvalidWeightException(String msg) { super(msg); }
}

class InvalidStatusException extends Exception {
    public InvalidStatusException(String msg) { super(msg); }
}


interface CourierService {
    double calculateCharges();
    void   displayDetails();
    void   updateStatus(String status) throws InvalidStatusException;
}

class Courier implements CourierService {

    private final int    courierId;
    private final String sender;
    private final String receiver;
    private       double weight;
    private       TransportMode mode;
    private       CourierStatus status;
    private       double charges;

    // ------------ constructor ------------
    public Courier(int courierId, String sender, String receiver,
                   double weight, String modeStr)
            throws InvalidTransportException, InvalidWeightException {

        this.courierId = courierId;
        this.sender    = sender;
        this.receiver  = receiver;

        setWeight(weight);
        setTransportMode(modeStr);
        this.status  = CourierStatus.BOOKED;   // default upon creation
        this.charges = calculateCharges();
    }

    
    private void setTransportMode(String modeStr) throws InvalidTransportException {
        try {
            this.mode = TransportMode.valueOf(modeStr.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidTransportException(
                "Transport mode \"" + modeStr + "\" is not available!");
        }
    }

    private void setWeight(double weight) throws InvalidWeightException {
        if (weight <= 0)
            throw new InvalidWeightException("Weight must be >0 kg.");
        if (weight > 100)
            throw new InvalidWeightException("Weight exceeds 100 kg limit!");
        this.weight = weight;
    }

    // ------------ interface implementation ------------
    @Override
    public double calculateCharges() {
        double ratePerKg = switch (mode) {
            case TRAIN  -> 10;
            case CAR    -> 15;
            case FLIGHT -> 30;
            case SHIP   -> 8;
        };
        return weight * ratePerKg;
    }

    @Override
    public void updateStatus(String status) throws InvalidStatusException {
        try {
            this.status = CourierStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new InvalidStatusException(
                "Invalid status \"" + status +
                "\". Valid: BOOKED, IN_TRANSIT, DELIVERED");
        }
    }

    @Override
    public void displayDetails() {
        System.out.println("Courier ID : " + courierId);
        System.out.println("Sender     : " + sender);
        System.out.println("Receiver   : " + receiver);
        System.out.println("Weight     : " + weight + " kg");
        System.out.println("Transport  : " + mode);
        System.out.println("Status     : " + status);
        System.out.println("Charges    : ₹" + charges);
        System.out.println("-------------------------------------------");
    }
}


class CourierManagementwithTracking {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Courier[] couriers = new Courier[5];
        int count = 0;

        // ---------- data-entry loop ----------
        while (true) {
            try {
                System.out.print("\nEnter Courier ID : ");
                int id = sc.nextInt(); sc.nextLine();

                System.out.print("Enter Sender Name : ");
                String sender = sc.nextLine();

                System.out.print("Enter Receiver Name : ");
                String receiver = sc.nextLine();

                System.out.print("Enter Weight (kg) : ");
                double weight = sc.nextDouble(); sc.nextLine();

                System.out.print("Enter Transport Mode (TRAIN/CAR/FLIGHT/SHIP): ");
                String mode = sc.nextLine();

                couriers[count++] = new Courier(id, sender, receiver, weight, mode);

            } catch (InvalidTransportException | InvalidWeightException ex) {
                System.out.println("Error: " + ex.getMessage());
            } catch (InputMismatchException ex) {
                System.out.println("Error: numeric input expected. Try again.");
                sc.nextLine(); // discard bad token
            } catch (ArrayIndexOutOfBoundsException ex) {
                System.out.println("Storage full (max 5 couriers).");
                break;
            }

            System.out.print("Add another courier? (yes/no): ");
            if (!sc.nextLine().equalsIgnoreCase("yes")) break;
        }

        
        for (int i = 0; i < count; i++) {
            try {
                System.out.print("Update status for Courier ID "
                                 + couriers[i].calculateCharges() /* unique ID */
                                 + " (BOOKED/IN_TRANSIT/DELIVERED): ");
                String newStatus = sc.nextLine();
                couriers[i].updateStatus(newStatus);
            } catch (InvalidStatusException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }

        
        System.out.println("\n----------- Courier Details -----------");
        for (int i = 0; i < count; i++) couriers[i].displayDetails();

        sc.close();
    }
}
