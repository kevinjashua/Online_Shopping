import java.util.*;

abstract class Product {
    private final String name;
    private final double price;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
    public abstract void displayDetails();
}

class Electronics extends Product {
    private final int warranty;

    public Electronics(String name, double price, int warranty) {
        super(name, price);
        this.warranty = warranty;
    }

    @Override
    public void displayDetails() {
        System.out.println("Electronics: " + getName() + " | Price: $" + getPrice() + " | Warranty: " + warranty + " months");
    }
}

class Clothing extends Product {
    private final String size;

    public Clothing(String name, double price, String size) {
        super(name, price);
        this.size = size;
    }

    @Override
    public void displayDetails() {
        System.out.println("Clothing: " + getName() + " | Price: $" + getPrice() + " | Size: " + size);
    }
}

interface DiscountStrategy {
    double applyDiscount(double price);
}

class PercentageDiscount implements DiscountStrategy {
    private final double percent;

    public PercentageDiscount(double percent) {
        this.percent = percent;
    }

    @Override
    public double applyDiscount(double price) {
        return price - (price * percent / 100);
    }
}

class FlatDiscount implements DiscountStrategy {
    private final double discountAmount;

    public FlatDiscount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    @Override
    public double applyDiscount(double price) {
        return Math.max(price - discountAmount, 0);
    }
}

class Cart {
    private final List<Product> products = new ArrayList<>();

    public void addProduct(Product product) {
        products.add(product);
        System.out.println(product.getName() + " added to cart.");
    }

    public double calculateTotal() {
        return products.stream().mapToDouble(Product::getPrice).sum();
    }

    public void displayCart() {
        System.out.println("\nCart Items:");
        products.forEach(Product::displayDetails);
        System.out.println("Total Price: $" + calculateTotal());
    }

    public List<Product> getProducts() { return products; }
}

class Order {
    private final Cart cart;
    private final DiscountStrategy discountStrategy;
    private String status;

    public Order(Cart cart, DiscountStrategy discountStrategy) {
        this.cart = cart;
        this.discountStrategy = discountStrategy;
        this.status = "Pending";
    }

    public void placeOrder() {
        double total = cart.calculateTotal();
        double finalPrice = discountStrategy.applyDiscount(total);
        System.out.println("\nOrder placed successfully!");
        System.out.println("Total before discount: $" + total);
        System.out.println("Total after discount: $" + finalPrice);
        this.status = "Completed";
    }

    public String getStatus() { return status; }
}

public class Online_Shopping {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Cart cart = new Cart();
            
            while (true) {
                System.out.println("\nChoose a product type to add: 1. Electronics 2. Clothing 3. Checkout");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter Electronics Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Price: ");
                        double price = scanner.nextDouble();
                        System.out.print("Enter Warranty (months): ");
                        int warranty = scanner.nextInt();
                        scanner.nextLine();
                        cart.addProduct(new Electronics(name, price, warranty));
                    }
                    case 2 -> {
                        System.out.print("Enter Clothing Name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter Price: ");
                        double price = scanner.nextDouble();
                        scanner.nextLine();
                        System.out.print("Enter Size: ");
                        String size = scanner.nextLine();
                        cart.addProduct(new Clothing(name, price, size));
                    }
                    case 3 -> {
                        System.out.println("\nChoose Discount Type: 1. Percentage 2. Flat");
                        int discountChoice = scanner.nextInt();
                        DiscountStrategy discountStrategy;
                        
                        if (discountChoice == 1) {
                            System.out.print("Enter Discount Percentage: ");
                            double percent = scanner.nextDouble();
                            discountStrategy = new PercentageDiscount(percent);
                        } else {
                            System.out.print("Enter Flat Discount Amount: ");
                            double amount = scanner.nextDouble();
                            discountStrategy = new FlatDiscount(amount);
                        }
                        
                        Order order = new Order(cart, discountStrategy);
                        order.placeOrder();
                        System.out.println("Order Status: " + order.getStatus());
                        return;
                    }
                    default -> System.out.println("Invalid choice! Try again.");
                }
            }
        }
    }
}
