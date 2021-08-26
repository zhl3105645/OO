import java.math.BigInteger;

public class Item {
    private final BigInteger coe;
    private final BigInteger index;
    private final BigInteger indexSin;
    private final BigInteger indexCos;

    public Item(BigInteger coe, BigInteger index, BigInteger indexSin, BigInteger indexCos) {
        this.coe = coe;
        this.index = index;
        this.indexSin = indexSin;
        this.indexCos = indexCos;
    }

    public BigInteger getCoe() {
        return coe;
    }

    public BigInteger getIndex() {
        return index;
    }

    public BigInteger getIndexSin() {
        return indexSin;
    }

    public BigInteger getIndexCos() {
        return indexCos;
    }

    public boolean zhiEquals(Item item) {
        return item.getCoe().equals(this.coe) && item.getIndex().equals(this.index)
                && item.getIndexSin().equals(this.indexSin)
                && item.getIndexCos().equals(this.indexCos);
    }

    public Item[] itemDer() {
        BigInteger coe1 = coe.multiply(index);
        BigInteger index1 = index.subtract(BigInteger.valueOf(1));
        Item item1 = new Item(coe1, index1, indexSin, indexCos);
        BigInteger coe2 = coe.multiply(indexSin);
        BigInteger indexSin2 = indexSin.subtract(BigInteger.valueOf(1));
        BigInteger indexCos2 = indexCos.add(BigInteger.valueOf(1));
        Item item2 = new Item(coe2, index, indexSin2, indexCos2);
        BigInteger coe3 = coe.multiply(indexCos).multiply(BigInteger.valueOf(-1));
        BigInteger indexSin3 = indexSin.add(BigInteger.valueOf(1));
        BigInteger indexCos3 = indexCos.subtract(BigInteger.valueOf(1));
        Item item3 = new Item(coe3, index, indexSin3, indexCos3);
        return new Item[]{item1, item2, item3};
    }

    public Item multItem(Item i) {
        BigInteger coe1 = coe.multiply(i.getCoe());
        BigInteger index1 = index.add(i.getIndex());
        BigInteger indexSin1 = indexSin.add(i.getIndexSin());
        BigInteger indexCos1 = indexCos.add(i.getIndexCos());
        return new Item(coe1, index1, indexSin1, indexCos1);
    }
    //打印单项式

    public String toString() {
        String s = "";
        Boolean flagPrint = false;
        if (coe.equals(BigInteger.valueOf(0))) {
            return s;
        } else {
            BigInteger b0 = BigInteger.valueOf(0);
            if (!(index.equals(b0) && indexSin.equals(b0) && indexCos.equals(b0))) {
                if (coe.equals(BigInteger.valueOf(1))) {
                    s = s + "+";
                } else if (coe.equals(BigInteger.valueOf(-1))) {
                    s = s + "-";
                } else {
                    s = s + "+" + coe.toString();
                    flagPrint = true;
                }
                if (!index.equals(b0)) {
                    if (flagPrint) {
                        s = s + "*";
                    }
                    s = s + "x";
                    flagPrint = true;
                    if (!index.equals(BigInteger.valueOf(1))) {
                        s = s + "**" + index.toString();
                    }
                }
                if (!indexSin.equals(b0)) {
                    if (flagPrint) {
                        s = s + "*";
                    }
                    s = s + "sin(x)";
                    flagPrint = true;
                    if (!indexSin.equals(BigInteger.valueOf(1))) {
                        s = s + "**" + indexSin.toString();
                    }
                }
                if (!indexCos.equals(b0)) {
                    if (flagPrint) {
                        s = s + "*";
                    }
                    s = s + "cos(x)";
                    flagPrint = true;
                    if (!indexCos.equals(BigInteger.valueOf(1))) {
                        s = s + "**" + indexCos.toString();
                    }
                }
            } else {
                s = "+" + coe.toString();
            }
        }
        s = s.replaceAll("\\+\\+", "+");
        s = s.replaceAll("\\+-", "-");
        return s;
    }
}
