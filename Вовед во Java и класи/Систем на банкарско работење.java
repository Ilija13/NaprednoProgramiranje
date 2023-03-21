import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

class Account {
    private long id;
    private String name;
    private double balance;
    private static DecimalFormat df = new DecimalFormat("0.00");

    public Account(String name, String balance) {
        this.name = name;
        this.balance = Double.parseDouble(balance.replace("$", ""));
        Random random = new Random();
        this.id = random.nextLong();
    }

    public double getDoubleBalance() {
        return balance;
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBalance() {
        return String.valueOf(balance + "$");
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id&&Objects.equals(name, account.name) && Objects.equals(balance, account.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, balance);
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nBalance: " + df.format(balance) + "$\n";
    }

    void withdraw(double totalAmount) throws Exception {
        if (balance >= totalAmount) {
            balance -= totalAmount;
        } else {
            throw new Exception();
        }
    }

    void deposit(double amount) {
        balance += amount;
    }
}

abstract class Transaction {
    protected final long fromId;
    protected final long toId;
    protected final String description;
    protected final double amount;

    public Transaction(long fromId, long toId, String description, String amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = Double.parseDouble(amount.replace("$", ""));
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    private DecimalFormat df = new DecimalFormat("0.00");

    public String getAmount() {
        return String.valueOf(df.format(amount)) + "$";
    }

    public double getDoubleAmount() {
        return amount;

    }

    public String getDescription() {
        return description;
    }


    public abstract double getProvision();

    double getTotalAmount() {
        return amount + getProvision();
    }

}

class FlatAmountProvisionTransaction extends Transaction {
    private double flatProvision;
    private static String str = "FlatAmount";

    FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatProvision) {
        super(fromId, toId, str, amount);
        this.flatProvision = Double.parseDouble(flatProvision.replace("$", ""));

    }

    @Override
    public double getProvision() {
        return this.flatProvision;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatAmountProvisionTransaction that = (FlatAmountProvisionTransaction) o;
        return Objects.equals(flatProvision, that.flatProvision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flatProvision);
    }

}

class FlatPercentProvisionTransaction extends Transaction {
    private int percent;
    private static String str = "FlatPercent";

    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, int percent) {
        super(fromId, toId, str, amount);
        this.percent = percent;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlatPercentProvisionTransaction that = (FlatPercentProvisionTransaction) o;
        return percent == that.percent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(percent);
    }

    @Override
    public double getProvision() {
        return Math.floor(amount) * percent / 100;
    }


}

class Bank {
    private Account[] accounts;
    private String name;
    private double provision;
    private double transfered;
    private DecimalFormat df = new DecimalFormat("0.00");

    public Bank(String name, Account[] accounts) {
        this.accounts = Arrays.copyOf(accounts, accounts.length);
        this.name = name;
        provision = 0;
        transfered = 0;
    }

    private Account findAccount(long id) {
        Account acc = null;
        for (Account account : accounts) {
            if (account.getId() == id) {
                acc = account;
            }
        }
        return acc;
    }


    public boolean makeTransaction(Transaction t) {
        Account from = findAccount(t.fromId);
        Account to = findAccount(t.toId);

        if (from == null || to == null) {
            return false;
        }

        try {
            from.withdraw(t.getTotalAmount());
            to.deposit(t.amount);
            transfered += t.getDoubleAmount();
            provision += t.getProvision();
            return true;
        } catch (Exception e) {
            return false;
        }


    }


    public Account[] getAccounts() {
        return accounts;
    }

    public String totalProvision() {
        return df.format(provision) + "$";
    }

    public String totalTransfers() {
        return df.format(transfered) + "$";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + name + "\n\n");
        for (Account account : accounts)
            sb.append(account.toString());
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Double.compare(bank.provision, provision) == 0 && Double.compare(bank.transfered, transfered) == 0 && Arrays.equals(accounts, bank.accounts) && Objects.equals(name, bank.name) && Objects.equals(df, bank.df);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, provision, transfered, df);
        result = 31 * result + Arrays.hashCode(accounts);
        return result;
    }
}

public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1) && !a1.equals(a2) && !a2.equals(a1) && !a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        //accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}
