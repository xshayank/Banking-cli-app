import bank_projec.BankAccount;
import bank_projec.InsufficientFundsException;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        ArrayList<BankAccount> accounts = loadAccountsFromFile("accounts.txt");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Bank System ---");
            System.out.println("1. View Accounts");
            System.out.println("2. Create Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 5) {
                System.out.println("Goodbye!");
                break;
            }

            if (choice == 1) {
                for (BankAccount acc : accounts) {
                    acc.displayAccountInfo();
                    System.out.println("--------------------------");
                }
                continue;
            }

            System.out.print("Enter account number: ");
            String accountNum = scanner.nextLine();

            BankAccount account = accounts.stream()
                    .filter(a -> a.getAccountNumber().equals(accountNum))
                    .findFirst()
                    .orElse(null);

            if (choice == 2) {
                System.out.print("Enter account holder name: ");
                String holder = scanner.nextLine();
                System.out.print("Enter initial balance: ");
                double initBalance = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                accounts.add(new BankAccount(accountNum, holder, initBalance));
                saveAccountsToFile(accounts, "accounts.txt");
                System.out.println("Account created successfully.");
                continue;
            }

            if (account == null) {
                System.out.println("Account not found.");
                continue;
            }

            switch (choice) {
                case 3:
                    System.out.print("Enter deposit amount: ");
                    double deposit = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    account.deposit(deposit);
                    saveAccountsToFile(accounts, "accounts.txt");
                    break;
                case 4:
                    System.out.print("Enter withdrawal amount: ");
                    double withdraw = scanner.nextDouble();
                    scanner.nextLine(); // Consume newline
                    try {
                        account.withdraw(withdraw);
                        saveAccountsToFile(accounts, "accounts.txt");
                    } catch (InsufficientFundsException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }

        scanner.close();
    }

    private static ArrayList<BankAccount> loadAccountsFromFile(String filename) {
        ArrayList<BankAccount> accounts = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String accNum = line;
                String holder = reader.readLine();
                double balance = Double.parseDouble(reader.readLine());
                // Skip separator
                reader.readLine();
                accounts.add(new BankAccount(accNum, holder, balance));
            }
        } catch (IOException e) {
            // File may not exist on first run, that's fine
        }
        return accounts;
    }

    private static void saveAccountsToFile(ArrayList<BankAccount> accounts, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (BankAccount account : accounts) {
                writer.write(account.getAccountNumber() + "\n");
                writer.write(account.getAccountHolderName() + "\n");
                writer.write(account.getBalance() + "\n");
                writer.write("---\n");
            }
        } catch (IOException e) {
            System.out.println("Error saving accounts: " + e.getMessage());
        }
    }
}