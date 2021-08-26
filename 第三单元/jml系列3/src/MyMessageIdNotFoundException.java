import com.oocourse.spec3.exceptions.MessageIdNotFoundException;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private static Count count = new Count();
    private int id;

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        count.addSum();
        count.addIdException(id);
    }

    @Override
    public void print() {
        int x = count.getSum();
        int y = count.getIdException(id);
        System.out.println("minf-" + x + ", " + id + "-" + y);
    }
}

