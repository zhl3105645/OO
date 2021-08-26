import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.io.Serializable;

public class Item implements Serializable {
    private Pow pow;
    private ArrayList<Triangle> triangles;//三角函数的list
    private ArrayList<String> expItem;//有表达式因子的list

    public Item(Pow pow, ArrayList<Triangle> triangles, ArrayList<String> expItem) {
        this.pow = pow;
        this.triangles = triangles;
        this.expItem = expItem;
    }

    public Pow getPow() {
        return pow;
    }

    public void setPow(Pow pow) {
        this.pow = pow;
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(ArrayList<Triangle> triangles) {
        this.triangles = triangles;
    }

    public ArrayList<String> getExpItem() {
        return expItem;
    }

    public void setExpItem(ArrayList<String> expItem) {
        this.expItem = expItem;
    }

    public void addTriangle(Triangle triangle) {
        if (triangle.getIndex().equals(BigInteger.valueOf(0))) {
            return;
        }
        for (Triangle triangle1 : triangles) {
            if (triangle.isSin() == triangle1.isSin()
                    && triangle.getFactor().equals(triangle1.getFactor())) {
                triangle1.setCoe(triangle1.getCoe().multiply(triangle.getCoe()));
                triangle1.setIndex(triangle1.getIndex().add(triangle.getIndex()));
                return;
            }
        }
        triangles.add(triangle);
    }

    @Override
    public String toString() {
        BigInteger coe = pow.getCoe();
        for (Triangle triangle : triangles) {
            coe = coe.multiply(triangle.getCoe());
        }
        Pow pow1 = new Pow(coe, pow.getIndex());
        StringBuilder sb = new StringBuilder();
        if (triangles.isEmpty() && expItem.isEmpty()
                && pow1.getIndex().equals(BigInteger.valueOf(0))) {
            sb.append(coe.toString());
        } else {
            boolean flag = false;//标记是否开始乘
            if (coe.equals(BigInteger.valueOf(-1))) {
                sb.append("-");
            } else if (!coe.equals(BigInteger.valueOf(1))) {
                sb.append(coe.toString());
                flag = true;
            }

            if (!pow1.getIndex().equals(BigInteger.valueOf(0))) {
                if (flag) {
                    sb.append("*");
                }
                flag = true;
                sb.append(pow1.toString());
            }
            for (Triangle triangle : triangles) {
                if (!triangle.getIndex().equals(BigInteger.valueOf(0))) { //sin(x)**0为1,无需输出
                    if (flag) {
                        sb.append("*").append(triangle.toString());
                    } else {
                        sb.append(triangle.toString());
                        flag = true;
                    }
                }
            }
            for (String string : expItem) {
                String s = "(" + string + ")";
                if (flag) {
                    sb.append("*").append(s);
                } else {
                    sb.append(s);
                    flag = true;
                }
            }
        }
        return sb.toString();
    }

    public ArrayList<Item> itemsDer() throws Exception {
        ArrayList<Item> items = new ArrayList<>();
        //powDer
        Item itemPowDer = (Item) this.deepCLone();
        Pow powDer = pow.derivation();
        itemPowDer.setPow(powDer);
        items.add(itemPowDer);
        //triangleDer
        for (Triangle triangle : triangles) {
            if (!triangle.isConstant()) {
                Item itemTriDer = (Item) this.deepCLone();
                ArrayList<Triangle> triangles1 = itemTriDer.getTriangles();
                //去除求导的那一项
                for (Triangle triangle1 : triangles1) {
                    if (triangle1.equals(triangle)) {
                        triangles1.remove(triangle1);
                        break;
                    }
                }
                //新增求导的两项
                Triangle[] triangle1 = triangle.derivation();
                for (Triangle value : triangle1) {
                    itemTriDer.addTriangle(value);
                }
                //String还需要加入factor的导数。
                String factor = triangle.getFactor();
                Process process = new Process();
                ArrayList<Item> items1 = process.getItems(factor);
                Poly poly = new Poly(items1);
                poly.getPolyDer();
                String s = poly.getPolyDerStr();
                ArrayList<String> expItem1 = itemTriDer.getExpItem();
                expItem1.add(s);
                items.add(itemTriDer);
            }
        }
        //expItemDer
        for (String string : expItem) {
            Item itemExpDer = (Item) this.deepCLone();
            Process process = new Process();
            ArrayList<Item> items1 = process.getItems(string);
            Poly poly = new Poly(items1);
            poly.getPolyDer();
            String s = poly.getPolyDerStr();
            ArrayList<String> expItem1 = itemExpDer.getExpItem();
            expItem1.remove(string);
            expItem1.add(s);
            items.add(itemExpDer);
        }
        return items;
    }

    public Object deepCLone() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(this);

        // 反序列化
        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);

        return ois.readObject();
    }

    public boolean simply() { //化简项
        ArrayList<String> strings = new ArrayList<>();
        for (String string : expItem) {
            Process process = new Process();
            ArrayList<Item> items = process.getItems(string);
            if (items.size() > 1) {
                strings.add(string);
            } else if (items.size() == 0) {
                return false;
            } else {
                Item item1 = items.get(0);
                pow = new Pow(pow.getCoe().multiply(item1.getPow().getCoe()),
                        pow.getIndex().add(item1.getPow().getIndex()));
                for (Triangle triangle : item1.triangles) {
                    this.addTriangle(triangle);
                }
                strings.addAll(item1.getExpItem());
            }
        }
        this.setExpItem(strings);
        ArrayList<Triangle> triangles1 = new ArrayList<>();
        for (Triangle triangle : triangles) {
            if (!triangle.getIndex().equals(BigInteger.valueOf(0))) {
                triangles1.add(triangle);
            }
        }
        this.setTriangles(triangles1);
        return true;
    }
}
