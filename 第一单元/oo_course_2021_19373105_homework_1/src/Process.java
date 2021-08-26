import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Process {

    //单项式提取str->single
    public Single getSingle(String singleString) {
        BigInteger xi = BigInteger.valueOf(1);
        BigInteger zhi = BigInteger.valueOf(0);

        String bianYin = "[+-]?x(\\*{2}[+-]?\\d+)?";
        String chanYin = "[+-]?\\d+";
        String yin = bianYin + "|" + chanYin;
        Pattern p = Pattern.compile(yin);
        Matcher m = p.matcher(singleString);
        while (m.find()) {
            String str = m.group();
            if (str.matches(bianYin)) {
                if (str.contains("**")) {
                    String[] strs = str.split("x\\*{2}");
                    if (strs.length == 1) {
                        zhi = zhi.add(new BigInteger(strs[0]));
                    } else if (strs.length == 2) {
                        zhi = zhi.add(new BigInteger(strs[1]));
                        if (strs[0].length() != 0) {
                            if (strs[0].charAt(0) == '-') {
                                xi = xi.multiply(BigInteger.valueOf(-1));
                            }
                        }
                    }
                } else {
                    zhi = zhi.add(BigInteger.valueOf(1));
                    if (str.charAt(0) == '-') {
                        xi = xi.multiply(BigInteger.valueOf(-1));
                    }
                }
            } else if (str.matches(chanYin)) {
                xi = xi.multiply(new BigInteger(str));
            }
        }
        return new Single(xi, zhi);
    }

}
