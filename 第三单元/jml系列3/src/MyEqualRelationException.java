import com.oocourse.spec3.exceptions.EqualRelationException;

public class MyEqualRelationException extends EqualRelationException {
    private static Count count = new Count();
    private int id1;
    private int id2;

    public MyEqualRelationException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        count.addSum();
        count.addIdException(id1);
        if (id1 != id2) {
            count.addIdException(id2);
        }
    }

    @Override
    public void print() {
        int x = count.getSum();
        int y = count.getIdException(id1);
        int z = count.getIdException(id2);
        System.out.println("er-" + x + ", " + id1 + "-" + y + ", " + id2 + "-" + z);
    }
}

