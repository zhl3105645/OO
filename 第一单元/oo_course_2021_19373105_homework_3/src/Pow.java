import java.io.Serializable;
import java.math.BigInteger;

public class Pow implements Serializable {
    private BigInteger coe;
    private BigInteger index;

    public Pow(BigInteger coe, BigInteger index) {
        this.coe = coe;
        this.index = index;
    }

    public BigInteger getCoe() {
        return coe;
    }

    public BigInteger getIndex() {
        return index;
    }

    public Pow derivation() {
        if (index.equals(BigInteger.valueOf(0))) {
            return new Pow(BigInteger.valueOf(0), BigInteger.valueOf(0));
        } else {
            BigInteger coe1 = coe.multiply(index);
            BigInteger index1 = index.subtract(BigInteger.valueOf(1));
            return new Pow(coe1, index1);
        }
    }

    @Override
    public String toString() { //只输出x**index
        if (coe.equals(BigInteger.valueOf(0))) {
            return "0";
        } else {
            String s;
            if (index.equals(BigInteger.valueOf(1))) {
                s = "x";
            } else {
                s = "x**" + index.toString();
            }
            return s;
        }
    }
}
