import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Process {
    public ArrayList<Item> getItems(String s) { //由字符串得到多项式
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<String> itemsStr = getItemsStr(s);
        /*for (String s1 : itemsStr) {
            System.out.println("s: " + s + " s1: " + s1);
        }*/
        for (String string : itemsStr) {
            ArrayList<String> triStrs = new ArrayList<>();
            ArrayList<String> expsStr = new ArrayList<>();
            for (int i = 0; i < string.length(); i++) {
                Judge judge = new Judge();
                if (string.charAt(i) == 's' || string.charAt(i) == 'c') {
                    //System.out.println("匹配三角函数");
                    int j = judge.isTri(string, i);
                    String triStr = string.substring(i, j + 1);
                    i = j;
                    triStrs.add(triStr);
                } else if (string.charAt(i) == '(') {
                    //System.out.println("匹配表达式");
                    int num = 1;
                    for (int k = i + 1; k < string.length(); k++) {
                        if (string.charAt(k) == '(') {
                            num++;
                        } else if (string.charAt(k) == ')') {
                            num--;
                            if (num == 0) {
                                String expStr = string.substring(i + 1, k);
                                expsStr.add(expStr);
                                i = k;
                                break;
                            }
                        }
                    }
                }
            }
            String s1 = string;
            for (String s2 : triStrs) {
                s1 = replace1(s1, s2);
            }
            for (String s2 : expsStr) {
                s1 = replace1(s1, s2);
            }
            Pow pow = getPow(s1);
            ArrayList<Triangle> triangles = getTri(triStrs);
            Item item = new Item(pow, triangles, expsStr);
            if (!pow.getCoe().equals(BigInteger.valueOf(0))) {
                items.add(item);
            }
        }
        return items;
    }

    ArrayList<String> getItemsStr(String polyStr) {
        ArrayList<String> strings = new ArrayList<>();
        int x = 0;
        int z = 0;//括号
        for (int i = 0; i < polyStr.length(); i++) {
            if (i == 0) {
                if (polyStr.charAt(0) == '(') {
                    z++;
                }
            }
            if (i == polyStr.length() - 1) {
                if (polyStr.charAt(i) == ')') {
                    z--;
                    if (z == 0) {
                        String s = polyStr.substring(x);
                        strings.add(s);
                        x = i + 1;
                    }
                } else {
                    String s = polyStr.substring(x);
                    strings.add(s);
                    x = i + 1;
                }
            }
            if (i != 0) {
                if (z == 0) {
                    if (polyStr.charAt(i) == '+' || polyStr.charAt(i) == '-') {
                        if (polyStr.charAt(i - 1) != '*' && polyStr.charAt(i - 1) != '(') {
                            String s = polyStr.substring(x, i);
                            strings.add(s);
                            x = i;
                        }
                    } else if (polyStr.charAt(i) == '(') {
                        z++;
                    }
                } else {
                    if (polyStr.charAt(i) == '(') {
                        z++;
                    } else if (polyStr.charAt(i) == ')') {
                        z--;
                    }
                }
            }
        }
        return strings;
    }

    ArrayList<Triangle> getTri(ArrayList<String> triStr) {
        ArrayList<Triangle> triangles = new ArrayList<>();
        if (triStr.isEmpty()) {
            return triangles;
        }
        for (String s : triStr) {
            String factor = "";
            BigInteger index = BigInteger.valueOf(1);
            int x = 0;//左括号位置
            int y = 0;//右括号位置
            int num = 0;//待匹配的括号数量
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '(') {
                    num++;
                    if (num == 1) {
                        x = i;
                    }
                } else if (s.charAt(i) == ')') {
                    num--;
                    if (num == 0) {
                        y = i;
                        factor = s.substring(x + 1, i);//不包括括号
                        break;
                    }
                }
            }
            if (y != s.length() - 1) {
                String indexStr = s.substring(y + 3);
                index = new BigInteger(indexStr);
            }
            boolean isSin = (s.startsWith("sin"));
            Triangle triangle = new Triangle(isSin, BigInteger.valueOf(1), index, factor);
            if (!index.equals(BigInteger.valueOf(0))) {
                triangles.add(triangle);
            }
        }
        return triangles;
    }

    Pow getPow(String itemStr) {
        if (itemStr.isEmpty()) {
            return new Pow(BigInteger.valueOf(1), BigInteger.valueOf(0));
        }
        Regex regex = new Regex();
        String yin = regex.getYin();
        String pow = regex.getPow();
        BigInteger index = BigInteger.valueOf(0);
        BigInteger coe = BigInteger.valueOf(1);
        Pattern p = Pattern.compile(yin);
        Matcher m = p.matcher(itemStr);
        while (m.find()) {
            String s = m.group();
            if (s.matches(pow)) {
                if (s.contains("**")) {
                    String[] strings = s.split("\\*\\*");
                    index = index.add(new BigInteger(strings[1]));
                } else {
                    index = index.add(BigInteger.valueOf(1));
                }
            } else {
                coe = coe.multiply(new BigInteger(s));
            }
        }
        if (itemStr.charAt(0) == '-') {
            if (itemStr.length() == 1 || !(itemStr.charAt(1) >= '0' && itemStr.charAt(1) <= '9')) {
                coe = coe.multiply(BigInteger.valueOf(-1));
            }
        }
        return new Pow(coe, index);
    }

    public String manageLine(String line) {
        String line1 = line.replaceAll("\\s*", "");//去除空白字符
        line1 = line1.replaceAll("\\+\\+", "+");
        line1 = line1.replaceAll("(\\+-)|(-\\+)", "-");
        line1 = line1.replaceAll("--", "+");
        line1 = line1.replaceAll("\\+\\+", "+");
        line1 = line1.replaceAll("(\\+-)|(-\\+)", "-");
        line1 = line1.replaceAll("--", "+");
        return line1;
    }

    public String replace1(String s1, String s2) {
        String s = "";
        for (int i = 0; i < s1.length(); i++) {
            if (s1.startsWith(s2, i)) {
                s = s1.substring(0, i) + s1.substring(i + s2.length());
                return s;
            }
        }
        return s;
    }

    public ArrayList<Item> merge(ArrayList<Item> items) {
        ArrayList<Item> items1 = new ArrayList<>();
        boolean[] flag = new boolean[items.size()];
        for (int i = 0; i < items.size(); i++) {
            flag[i] = false;
        }
        for (int i = 0; i < items.size(); i++) {
            if (!flag[i]) {
                Item item1 = items.get(i);
                for (int j = 0; j < items.size(); j++) {
                    Item item = items.get(j);
                    if (item.getExpItem().isEmpty() && item.getTriangles().isEmpty()
                            && item.getPow().getIndex().equals(item1.getPow().getIndex())) {
                        Pow pow = new Pow(item1.getPow().getCoe().add(item.getPow().getCoe()),
                                item.getPow().getIndex());
                        item1.setPow(pow);
                    }
                }
                items1.add(item1);
            }
        }
        return items1;
    }
}
