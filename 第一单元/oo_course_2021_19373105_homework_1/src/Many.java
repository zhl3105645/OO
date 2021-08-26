import java.math.BigInteger;
import java.util.ArrayList;

public class Many {
    private ArrayList<Single> singles = new ArrayList<Single>();

    //添加单项项
    public void addSingle(Single single) {
        if (single.getXi().equals(BigInteger.valueOf(0))) {
            return;
        } else {
            for (int i = 0; i < singles.size(); i++) {
                Single single1 = singles.get(i);
                if (single.getZhi().compareTo(single1.getZhi()) < 0) {
                    singles.add(i, single);
                    return;
                } else if (single.getZhi().compareTo(single1.getZhi()) == 0) {
                    single1.setXi(single1.getXi().add(single.getXi()));
                    singles.set(i, single1);
                    return;
                }
            }
            singles.add(single);
        }
        return;
    }

    //多项式求导
    public String manyDao() {
        if (singles.isEmpty()) {
            return "0";
        } else if (singles.size() == 1) {
            if (singles.get(0).getZhi() == BigInteger.valueOf(0)) {
                return "0";
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < singles.size(); i++) {
            Single single = singles.get(i);
            single.singleDao();
            sb.append(single.getDaoString());
        }
        return sb.toString();
    }

}
