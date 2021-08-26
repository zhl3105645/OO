public class Sprinkler extends Vehicle implements Engineered {
    private int volume;

    Sprinkler(int id, int price, int volume) {
        // TODO
        super(id, price);
        this.volume = volume;
    }

    @Override
    public void run() {
        System.out.println("Wow, I can Run and clear the road!");
    }

    @Override
    public int getPrice() {
        // TODO
        int p = 2 * super.getPrice();
        System.out.println("price is: " + p);
        return p;
    }

    @Override
    public void work() {
        System.out.println("Splashing!" + " " + this.volume + "L water used!");
    }
}
