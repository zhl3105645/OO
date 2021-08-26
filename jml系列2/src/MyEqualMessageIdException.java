import com.oocourse.spec2.exceptions.EqualMessageIdException;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static Count count = new Count();
    private int id;

    public MyEqualMessageIdException(int id) {
        this.id = id;
        count.addSum();
        count.addIdException(id);
    }

    @Override
    public void print() {
        int x = count.getSum();
        int y = count.getIdException(id);
        System.out.println("emi-" + x + ", " + id + "-" + y);
    }
}
