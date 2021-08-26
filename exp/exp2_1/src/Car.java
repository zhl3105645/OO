public class Car extends Vehicle {
    private int maxSpeed;

    Car(int id, int price, int maxSpeed) {
        // TODO
        super(id, price);
        this.maxSpeed = maxSpeed;
    }

    @Override
    public void run() {
        System.out.println("Wow, I can Run (maxSpeed:" + maxSpeed + ")!");
    }

    @Override
    public int getPrice() {
        // TODO
        if (maxSpeed < 1000) {
            int p = super.getPrice();
            System.out.println("price is: " + p);
            return p;
        } else {
            int p = super.getPrice() + 1000;
            System.out.println("price is: " + p);
            return p;
        }
    }
}
