package payment;

public class CashPayment implements PaymentMethod {
    private double cashGiven;
    private double change;

    public CashPayment(double cashGiven) {
        this.cashGiven = cashGiven;
    }

    @Override
    public void processPayment(double amount) {
        this.change = cashGiven - amount;
        System.out.println("=== PEMBAYARAN TUNAI ===");
        System.out.printf("Tagihan   : Rp %.0f%n", amount);
        System.out.printf("Dibayar   : Rp %.0f%n", cashGiven);
        System.out.printf("Kembalian : Rp %.0f%n", change);
        System.out.println("========================");
    }

    @Override
    public String getPaymentType() { return "Tunai (Cash)"; }

    public double getCashGiven() { return cashGiven; }
    public double getChange() { return change; }
}
