import com.oocourse.spec2.exceptions.EqualGroupIdException;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static Count count = new Count();
    private int id;

    public MyEqualGroupIdException(int id) {
        this.id = id;
        count.addSum();
        count.addIdException(id);
    }

    @Override
    public void print() {
        int x = count.getSum();
        int y = count.getIdException(id);
        System.out.println("egi-" + x + ", " + id + "-" + y);
    }
}
