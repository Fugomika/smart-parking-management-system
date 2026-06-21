package payment;

public interface PaymentMethod {
    void processPayment(double amount);
    String getPaymentType();
}
