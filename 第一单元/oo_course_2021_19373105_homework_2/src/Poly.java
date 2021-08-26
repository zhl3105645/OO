import java.math.BigInteger;
import java.util.ArrayList;

public class Poly {
    private final ArrayList<Item> items = new ArrayList<>();
    private final ArrayList<Item> itemsDer = new ArrayList<>();

    public ArrayList<Item> getItemsDer() {
        return itemsDer;
    }

    //增加item到items,不增加|合并同类项|增加新项
    public void addItem(Item item) {
        BigInteger coe = item.getCoe();
        BigInteger index = item.getIndex();
        BigInteger indexSin = item.getIndexSin();
        BigInteger indexCos = item.getIndexCos();
        if (!coe.equals(BigInteger.valueOf(0))) {
            for (int i = 0; i < items.size(); i++) {
                Item item1 = items.get(i);
                BigInteger coe1 = item1.getCoe();
                BigInteger index1 = item1.getIndex();
                BigInteger indexSin1 = item1.getIndexSin();
                BigInteger indexCos1 = item1.getIndexCos();
                if (index.equals(index1) && indexSin.equals(indexSin1)
                        && indexCos.equals(indexCos1)) {
                    Item item2 = new Item(coe.add(coe1), index, indexSin, indexCos);
                    items.set(i, item2);
                    return;
                }
            }
            items.add(item);
        }
    }

    public void addItemDer(Item item) {
        BigInteger coe = item.getCoe();
        BigInteger index = item.getIndex();
        BigInteger indexSin = item.getIndexSin();
        BigInteger indexCos = item.getIndexCos();
        if (!coe.equals(BigInteger.valueOf(0))) {
            for (int i = 0; i < itemsDer.size(); i++) {
                Item item1 = itemsDer.get(i);
                BigInteger coe1 = item1.getCoe();
                BigInteger index1 = item1.getIndex();
                BigInteger indexSin1 = item1.getIndexSin();
                BigInteger indexCos1 = item1.getIndexCos();
                if (index.equals(index1) && indexSin.equals(indexSin1)
                        && indexCos.equals(indexCos1)) {
                    Item item2 = new Item(coe.add(coe1), index, indexSin, indexCos);
                    itemsDer.set(i, item2);
                    return;
                }
            }
            itemsDer.add(item);
        }
    }

    //poly求导
    public void polyDer() {
        if (items.isEmpty()) {
            return;
        } else if (items.size() == 1) {
            BigInteger index = items.get(0).getIndex();
            BigInteger indexSin = items.get(0).getIndexSin();
            BigInteger indexCos = items.get(0).getIndexCos();
            if (index.equals(BigInteger.valueOf(0)) && indexSin.equals(BigInteger.valueOf(0))
                    && indexCos.equals(BigInteger.valueOf(0))) {
                return;
            }
        }
        for (Item item : items) {
            Item[] items1 = item.itemDer();
            for (Item value : items1) {
                addItemDer(value);
            }
        }
    }

    //打印多项式
    public String derToString() {
        StringBuilder sb = new StringBuilder();
        if (itemsDer.isEmpty()) {
            return "0";
        }
        for (Item item : itemsDer) {
            sb.append(item.toString());
        }
        return sb.toString();
    }
}
