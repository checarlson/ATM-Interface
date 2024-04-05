import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

class Transaction {
    private String type;
    private double amount;
    private String timestamp;

    public Transaction(String type, double amount, String timestamp) {
        this.type = type;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

class TransferTransaction extends Transaction {
    private String recipientId;

    public TransferTransaction(String type, double amount, String timestamp, String recipientId) {
        super(type, amount, timestamp);
        this.recipientId = recipientId;
    }

    public String getRecipientId() {
        return recipientId;
    }
}

class Account {
    private String userId;
    private int pin;
    private double balance;
    private ArrayList<Transaction> transactions; // Added type parameter

    public Account(String userId, int pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public boolean validateUser(String userId, int pin) {
        return this.userId.equals(userId) && this.pin == pin;
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add(new Transaction("Deposit", amount, getCurrentTimestamp()));
    }

    public double balance(){
        return balance;
    }

    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            transactions.add(new Transaction("Withdrawal", amount, getCurrentTimestamp()));
            return true;
        }
        return false;
    }

    public boolean transfer(String recipientId, double amount) {
        if (balance >= amount) {
            balance -= amount;
            transactions.add(new TransferTransaction("Transfer", amount, getCurrentTimestamp(), recipientId));
            return true;
        }
        return false;
    }

    public ArrayList<Transaction> getTransactions() { // Changed return type
        return transactions;
    }

    private String getCurrentTimestamp() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        // System.out.println(dtf.format(now));
        return dtf.format(now); // Placeholder for timestamp logic
    }
}

class ATMInterface {
    private static final String USER_ID = "user123";
    private static final int PIN = 1234;
    private static final double INITIAL_BALANCE = 1000.0;

    private static Account account;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        account = new Account(USER_ID, PIN, INITIAL_BALANCE);
        runATM();
    }

    private static void runATM() {
        while (true) {
            System.out.print("Enter user ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter PIN: ");
            int pin = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            if (account.validateUser(userId, pin)) {
                displayMenu();
            } else {
                System.out.println("Invalid user ID or PIN. Please try again.");
            }
        }
    }

    private static void displayMenu() {
        while (true) {
            System.out.println("\nATM Menu:");
            System.out.println("1. View Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Account Balance");
            System.out.println("6. Quit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    displayTransactionHistory();
                    break;
                case 2:
                    performWithdrawal();
                    break;
                case 3:
                    performDeposit();
                    break;
                case 4:
                    performTransfer();
                    break;
                case 5:
                    displayBalance();
                    break;
                case 6:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    System.exit(0); // Terminate the program
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 6.");
            }
        }
    }

    private static void displayTransactionHistory() {
        ArrayList<Transaction> transactions = account.getTransactions();
        System.out.println("\nTransaction History:");
        System.out.println("-------------------------------");
        System.out.println("Type\t\tAmount\t\tTimestamp");
        System.out.println("-------------------------------");
        for (Transaction transaction : transactions) {
            System.out.printf("%s\t\t%.2f\t\t%s\n", transaction.getType(), transaction.getAmount(), transaction.getTimestamp());
        }
        System.out.println("-------------------------------");
    }

    private static void performWithdrawal() {
        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character

        if (account.withdraw(amount)) {
            System.out.println("Withdrawal successful.");
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    private static void performDeposit() {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character

        account.deposit(amount);
        System.out.println("Deposit successful.");
    }

    private static void performTransfer() {
        System.out.print("Enter recipient ID: ");
        String recipientId = scanner.nextLine();
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline character

        if (account.transfer(recipientId, amount)) {
            System.out.println("Transfer successful.");
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    private static void displayBalance(){
        System.out.println("Account Balance: "+account.balance());
    }
}
