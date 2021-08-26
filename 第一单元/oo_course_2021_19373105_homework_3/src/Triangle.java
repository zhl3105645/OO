import java.io.Serializable;
import java.math.BigInteger;

public class Triangle implements Serializable {
    private boolean isSin;
    private BigInteger coe;
    private BigInteger index;
    private String factor;

    public Triangle(boolean isSin, BigInteger coe, BigInteger index, String factor) {
        this.isSin = isSin;
        this.coe = coe;
        this.index = index;
        this.factor = factor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Triangle triangle = (Triangle) o;

        return this.isSin == triangle.isSin
                && this.getCoe().equals(triangle.getCoe())
                && this.getIndex().equals(triangle.getIndex())
                && this.getFactor().equals(triangle.getFactor());
    }

    public boolean isSin() {
        return isSin;
    }

    public BigInteger getCoe() {
        return coe;
    }

    public void setCoe(BigInteger coe) {
        this.coe = coe;
    }

    public BigInteger getIndex() {
        return index;
    }

    public void setIndex(BigInteger index) {
        this.index = index;
    }

    public String getFactor() {
        return factor;
    }

    //不输出factor的求导
    public Triangle[] derivation() {
        if (index.equals(BigInteger.valueOf(0))) {
            return new Triangle[]{new Triangle(isSin, coe, index, factor)};
        } else {
            Triangle triangle1 = new Triangle(isSin, coe.multiply(index),
                    index.subtract(BigInteger.valueOf(1)), factor);
            Triangle triangle2;
            if (isSin) {
                triangle2 = new Triangle(!isSin, BigInteger.valueOf(1),
                        BigInteger.valueOf(1), factor);
            } else {
                triangle2 = new Triangle(!isSin, BigInteger.valueOf(-1),
                        BigInteger.valueOf(1), factor);
            }
            return new Triangle[]{triangle1, triangle2};
        }
    }

    //判断三角函数是否是常量,若是常量，factor不需要求导
    public Boolean isConstant() {
        return index.equals(BigInteger.valueOf(0)) || !factor.contains("x");
    }

    @Override//只输出sin()**index
    public String toString() {
        String s;
        if (index.equals(BigInteger.valueOf(1))) {
            if (isSin) {
                s = "sin(" + factor + ")";
            } else {
                s = "cos(" + factor + ")";
            }
            return s;
        } else {
            if (isSin) {
                s = "sin(" + factor + ")" + "**" + index.toString();
            } else {
                s = "cos(" + factor + ")" + "**" + index.toString();
            }
            return s;
        }
    }
}
