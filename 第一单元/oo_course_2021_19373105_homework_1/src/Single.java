import java.math.BigInteger;

public class Single {
    private BigInteger xi;
    private BigInteger zhi;
    private BigInteger xiDao;
    private BigInteger zhiDao;

    //构造方法
    public Single(BigInteger xi, BigInteger zhi) {
        this.xi = xi;
        this.zhi = zhi;
    }

    public BigInteger getXi() {
        return xi;
    }

    public BigInteger getZhi() {
        return zhi;
    }

    public void setXi(BigInteger xi) {
        this.xi = xi;
    }

    public void setZhi(BigInteger zhi) {
        this.zhi = zhi;
    }

    public BigInteger getXiDao() {
        return xiDao;
    }

    public BigInteger getZhiDao() {
        return zhiDao;
    }

    //单项式求导
    public void singleDao() {
        if (zhi.equals(BigInteger.valueOf(0)) || xi.equals(BigInteger.valueOf(0))) {
            xiDao = BigInteger.valueOf(0);
            zhiDao = BigInteger.valueOf(0);
        } else {
            xiDao = xi.multiply(zhi);
            zhiDao = zhi.subtract(BigInteger.ONE);
        }
    }

    //字符串拼接
    public String getDaoString() {
        String daoString = "";
        if (xiDao.equals(BigInteger.valueOf(0))) {
            daoString = "";
        } else if (xiDao.compareTo(BigInteger.valueOf(0)) > 0) {
            if (zhiDao.equals(BigInteger.valueOf(0))) {
                daoString = "+" + xiDao.toString();
            } else if (zhiDao.equals(BigInteger.valueOf(1))) {
                if (xiDao.equals(BigInteger.valueOf(1))) {
                    daoString = "+" + "x";
                } else {
                    daoString = "+" + xiDao.toString() + "*" + "x";
                }
            } else {
                if (xiDao.equals(BigInteger.valueOf(1))) {
                    daoString = "+" + "x**" + zhiDao.toString();
                } else {
                    daoString = "+" + xiDao.toString() + "*" + "x**" + zhiDao.toString();
                }
            }
        } else {
            if (zhiDao.equals(BigInteger.valueOf(0))) {
                daoString = "-" + xiDao.abs().toString();
            } else if (zhiDao.equals(BigInteger.valueOf(1))) {
                if (xiDao.equals(BigInteger.valueOf(-1))) {
                    daoString = "-" + "x";
                } else {
                    daoString = "-" + xiDao.abs().toString() + "*" + "x";
                }
            } else {
                if (xiDao.equals(BigInteger.valueOf(-1))) {
                    daoString = "-" + "x**" + zhiDao.toString();
                } else {
                    daoString = "-" + xiDao.abs().toString() + "*" + "x**" + zhiDao.toString();
                }
            }
        }
        //System.out.println(daoString);
        return daoString;
    }

}
