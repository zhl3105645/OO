import com.oocourse.spec2.exceptions.GroupIdNotFoundException;

public class MyGroupIdNotFoundException extends GroupIdNotFoundException {
    private static Count count = new Count();
    private int id;

    public MyGroupIdNotFoundException(int id) {
        this.id = id;
        count.addSum();
        count.addIdException(id);
    }

    @Override
    public void print() {
        int x = count.getSum();
        int y = count.getIdException(id);
        System.out.println("ginf-" + x + ", " + id + "-" + y);
    }
}
