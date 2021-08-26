import com.oocourse.spec3.exceptions.EqualEmojiIdException;

public class MyEqualEmojiIdException extends EqualEmojiIdException {
    private static Count count = new Count();
    private int id;

    public MyEqualEmojiIdException(int id) {
        this.id = id;
        count.addSum();
        count.addIdException(id);
    }

    @Override
    public void print() {
        int x = count.getSum();
        int y = count.getIdException(id);
        System.out.println("eei-" + x + ", " + id + "-" + y);
    }
}
