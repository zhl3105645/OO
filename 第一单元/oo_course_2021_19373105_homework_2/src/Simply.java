import java.math.BigInteger;
import java.util.ArrayList;

public class Simply {
    public ArrayList<String> simply(ArrayList<Item> items) {
        ArrayList<String> itemStr = new ArrayList<>();
        ArrayList<Item> itemsSelect = new ArrayList<>();
        //提取sincos都相等的项
        for (int i = 0; i < items.size(); i++) {
            ArrayList<Item> itemsSinCos = getSinCosArr(i, items, itemsSelect);
            if (itemsSinCos.size() > 1) {
                itemStr.add(getSinCosStr(itemsSinCos));
                itemsSelect.addAll(itemsSinCos);
            }
        }
        //提取sin相等cos不等的项
        for (int i = 0; i < items.size(); i++) {
            ArrayList<Item> itemsSin = getSinArr(i, items, itemsSelect);
            if (itemsSin.size() > 1) {
                itemStr.add(getSinStr(itemsSin));
                itemsSelect.addAll(itemsSin);
            }
        }
        //提取cos相等sin不等的项
        for (int i = 0; i < items.size(); i++) {
            ArrayList<Item> itemsCos = getCosArr(i, items, itemsSelect);
            if (itemsCos.size() > 1) {
                itemStr.add(getCosStr(itemsCos));
                itemsSelect.addAll(itemsCos);
            }
        }
        //提取sincos不等x相等的项
        for (int i = 0; i < items.size(); i++) {
            ArrayList<Item> itemsX = getXArr(i, items, itemsSelect);
            if (itemsX.size() > 1) {
                itemStr.add(getXStr(itemsX));
                itemsSelect.addAll(itemsX);
            }
        }
        for (Item item : items) {
            if (!itemsSelect.contains(item) && !item.getCoe().equals(BigInteger.valueOf(0))) {
                itemStr.add(item.toString());
            }
        }
        return itemStr;
    }

    public ArrayList<Item> getSinCosArr(int i, ArrayList<Item> items, ArrayList<Item> itemsSelect) {
        ArrayList<Item> itemsSinCos = new ArrayList<>();
        if (!inItem(itemsSelect, items.get(i))
                && !items.get(i).getCoe().equals(BigInteger.valueOf(0))
                && !items.get(i).getIndexSin().equals(BigInteger.valueOf(0))
                && !items.get(i).getIndexCos().equals(BigInteger.valueOf(0))) {
            itemsSinCos.add(items.get(i));
            for (int j = i + 1; j < items.size(); j++) {
                if (!inItem(itemsSelect, items.get(j))
                        && !items.get(j).getCoe().equals(BigInteger.valueOf(0))
                        && items.get(i).getIndexSin().equals(items.get(j).getIndexSin())
                        && items.get(i).getIndexCos().equals(items.get(j).getIndexCos())) {
                    itemsSinCos.add(items.get(j));
                }
            }
        }
        return itemsSinCos;
    }

    public ArrayList<Item> getSinArr(int i, ArrayList<Item> items, ArrayList<Item> itemsSelect) {
        ArrayList<Item> itemsSin = new ArrayList<>();
        if (!inItem(itemsSelect, items.get(i))
                && !items.get(i).getCoe().equals(BigInteger.valueOf(0))
                && !items.get(i).getIndexSin().equals(BigInteger.valueOf(0))) {
            itemsSin.add(items.get(i));
            for (int j = i + 1; j < items.size(); j++) {
                if (!inItem(itemsSelect, items.get(j))
                        && !items.get(j).getCoe().equals(BigInteger.valueOf(0))
                        && items.get(i).getIndexSin().equals(items.get(j).getIndexSin())) {
                    if (items.get(i).getIndexCos().equals(BigInteger.valueOf(0))
                            && items.get(j).getIndexCos().equals(BigInteger.valueOf(0))) {
                        itemsSin.add(items.get(j));
                    } else if (!items.get(i).getIndexCos().equals(items.get(j).getIndexCos())) {
                        itemsSin.add(items.get(j));
                    }
                }
            }
        }
        return itemsSin;
    }

    public ArrayList<Item> getCosArr(int i, ArrayList<Item> items, ArrayList<Item> itemsSelect) {
        ArrayList<Item> itemsCos = new ArrayList<>();
        if (!inItem(itemsSelect, items.get(i))
                && !items.get(i).getCoe().equals(BigInteger.valueOf(0))
                && !items.get(i).getIndexCos().equals(BigInteger.valueOf(0))) {
            itemsCos.add(items.get(i));
            for (int j = i + 1; j < items.size(); j++) {
                if (!inItem(itemsSelect, items.get(j))
                        && !items.get(j).getCoe().equals(BigInteger.valueOf(0))
                        && items.get(i).getIndexCos().equals(items.get(j).getIndexCos())) {
                    if (items.get(i).getIndexSin().equals(BigInteger.valueOf(0))
                            && items.get(j).getIndexSin().equals(BigInteger.valueOf(0))) {
                        itemsCos.add(items.get(j));
                    } else if (!items.get(i).getIndexSin().equals(items.get(j).getIndexSin())) {
                        itemsCos.add(items.get(j));
                    }
                }
            }
        }
        return itemsCos;
    }

    public ArrayList<Item> getXArr(int i, ArrayList<Item> items, ArrayList<Item> itemsSelect) {
        ArrayList<Item> itemsX = new ArrayList<>();
        if (!inItem(itemsSelect, items.get(i))
                && !items.get(i).getCoe().equals(BigInteger.valueOf(0))
                && !items.get(i).getIndex().equals(BigInteger.valueOf(0))) {
            itemsX.add(items.get(i));
            for (int j = i + 1; j < items.size(); j++) {
                if (!inItem(itemsSelect, items.get(j))
                        && !items.get(j).getCoe().equals(BigInteger.valueOf(0))
                        && items.get(i).getIndex().equals(items.get(j).getIndex())) {
                    itemsX.add(items.get(j));
                }
            }
        }
        return itemsX;
    }

    public String getSinCosStr(ArrayList<Item> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Item item : items) {
            Item item1 = new Item(item.getCoe(), item.getIndex(),
                    BigInteger.valueOf(0), BigInteger.valueOf(0));
            sb.append(item1.toString());
        }
        sb.append(")");
        sb.append("*sin(x)");
        if (!items.get(0).getIndexSin().equals(BigInteger.valueOf(1))) {
            sb.append("**").append(items.get(0).getIndexSin().toString());
        }
        if (!items.get(0).getIndexCos().equals(BigInteger.valueOf(0))) {
            sb.append("*cos(x)");
            if (!items.get(0).getIndexCos().equals(BigInteger.valueOf(1))) {
                sb.append("**").append(items.get(0).getIndexCos().toString());
            }
        }
        return sb.toString();
    }

    public String getSinStr(ArrayList<Item> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Item item : items) {
            Item item1 = new Item(item.getCoe(), item.getIndex(),
                    BigInteger.valueOf(0), item.getIndexCos());
            sb.append(item1.toString());
        }
        sb.append(")");
        sb.append("*sin(x)");
        if (!items.get(0).getIndexSin().equals(BigInteger.valueOf(1))) {
            sb.append("**").append(items.get(0).getIndexSin().toString());
        }
        return sb.toString();
    }

    public String getCosStr(ArrayList<Item> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Item item : items) {
            Item item1 = new Item(item.getCoe(), item.getIndex(),
                    item.getIndexSin(), BigInteger.valueOf(0));
            sb.append(item1.toString());
        }
        sb.append(")");
        sb.append("*cos(x)");
        if (!items.get(0).getIndexCos().equals(BigInteger.valueOf(1))) {
            sb.append("**").append(items.get(0).getIndexCos().toString());
        }
        return sb.toString();
    }

    public String getXStr(ArrayList<Item> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (Item item : items) {
            Item item1 = new Item(item.getCoe(), BigInteger.valueOf(0),
                    item.getIndexSin(), item.getIndexCos());
            sb.append(item1.toString());
        }
        sb.append(")*x");
        if (!items.get(0).getIndex().equals(BigInteger.valueOf(1))) {
            sb.append("**").append(items.get(0).getIndex().toString());
        }
        return sb.toString();
    }

    ///合并项的字符串
    public String mergeStr(ArrayList<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append("+").append(s);
        }
        String s = sb.toString();
        s = s.replaceAll("\\+\\+", "+");
        s = s.replaceAll("\\+-", "-");
        s = s.replaceAll("\\(\\+", "(");
        if (s.charAt(0) == '+') {
            s = s.substring(1);
        }
        return s;
    }

    public Boolean inItem(ArrayList<Item> items, Item item) {
        for (Item item1 : items) {
            if (item.zhiEquals(item1)) {
                return true;
            }
        }
        return false;
    }
}
