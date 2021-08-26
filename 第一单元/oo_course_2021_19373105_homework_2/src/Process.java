import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Process {
    public String manageLine(String line) {
        String line1 = line.replaceAll("\\s*", "");//去除空白字符
        line1 = line1.replaceAll("\\+\\+", "+");
        line1 = line1.replaceAll("(\\+-)|(-\\+)", "-");
        line1 = line1.replaceAll("--", "+");
        line1 = line1.replaceAll("\\+\\+", "+");
        line1 = line1.replaceAll("(\\+-)|(-\\+)", "-");
        line1 = line1.replaceAll("--", "+");
        line1 = line1.replaceAll("\\(x\\)", "x");
        return line1;
    }

    public ArrayList<Item> getItems(String polyStr) {
        ArrayList<Item> items = new ArrayList<>();
        ArrayList<String> itemsStrs = getItemsStr(polyStr);
        //System.out.println("polyStr: " + polyStr);
        for (String itemStr : itemsStrs) {
            //System.out.println("itemStr: " + itemStr);///
            ArrayList<String> expsStr = getExpsStr(itemStr);
            if (expsStr.isEmpty()) {
                //System.out.println("进入wu表达式因子的函数");
                items.add(getNoExp(itemStr));
            } else {
                //System.out.println("进入有表达式因子的函数");
                ArrayList<Item> items1 = getExp(itemStr);
                items.addAll(items1);
            }
        }
        return items;
    }

    public Item getNoExp(String str) {
        BigInteger coe = BigInteger.valueOf(1);
        BigInteger index = BigInteger.valueOf(0);
        BigInteger indexSin = BigInteger.valueOf(0);
        BigInteger indexCos = BigInteger.valueOf(0);
        Regex regex = new Regex();
        String yin = regex.getYin();
        String sin = regex.getSin();
        String cos = regex.getCos();
        String pow = regex.getPow();
        Pattern p = Pattern.compile(yin);
        Matcher m = p.matcher(str);
        while (m.find()) {
            String s = m.group();
            if (s.matches(sin)) {
                if (s.contains("**")) {
                    String[] strings = s.split("\\*\\*");
                    indexSin = indexSin.add(new BigInteger(strings[1]));
                } else {
                    indexSin = indexSin.add(BigInteger.valueOf(1));
                }
            } else if (s.matches(cos)) {
                if (s.contains("**")) {
                    String[] strings = s.split("\\*\\*");
                    indexCos = indexCos.add(new BigInteger(strings[1]));
                } else {
                    indexCos = indexCos.add(BigInteger.valueOf(1));
                }
            } else if (s.matches(pow)) {
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
        if (str.charAt(0) == '-' && !(str.charAt(1) >= '0' && str.charAt(1) <= '9')) {
            coe = coe.multiply(BigInteger.valueOf(-1));
        }
        return new Item(coe, index, indexSin, indexCos);
    }

    public ArrayList<Item> getExp(String expStr) {
        ArrayList<Item> items;
        ArrayList<String> expsStr = getExpsStr(expStr);
        String noExpStr = expStr;
        for (String s : expsStr) {
            //System.out.println("expStr: " + s);
            noExpStr = noExpStr.replace(s, " ");
        }
        //System.out.println("noExpStr: " + noExpStr);///
        Item item = getNoExp(noExpStr);
        //System.out.println("coe: " + item.getCoe() + " index: " + item.getIndex() +
        //" indexSin: " + item.getIndexSin() + " indexCos: " + item.getIndexCos());///
        //System.out.println("exp[0]: " + expsStr.get(0));///
        String polyStr = expsStr.get(0).substring(1, expsStr.get(0).length() - 1);
        //System.out.println("PolySTr: " + polyStr);//
        items = getItems(polyStr);
        for (int i = 1; i < expsStr.size(); i++) {
            String polyStr1 = expsStr.get(i).substring(1, expsStr.get(i).length() - 1);
            ArrayList<Item> items1 = getItems(polyStr1);
            items = multItems(items, items1);
        }
        ArrayList<Item> items1 = new ArrayList<>();
        for (Item item1 : items) {
            Item item2 = item1.multItem(item);
            items1.add(item2);
        }
        return items1;
    }

    public ArrayList<Item> multItems(ArrayList<Item> items1, ArrayList<Item> items2) {
        ArrayList<Item> items = new ArrayList<>();
        for (Item item1 : items1) {
            for (Item item2 : items2) {
                Item item = item1.multItem(item2);
                items.add(item);
            }
        }
        return items;
    }

    public ArrayList<String> getItemsStr(String polyStr) {
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
                        String s = polyStr.substring(x, polyStr.length());
                        strings.add(s);
                        x = i + 1;
                    }
                } else {
                    String s = polyStr.substring(x, polyStr.length());
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

    public ArrayList<String> getExpsStr(String itemStr) {
        ArrayList<String> strings = new ArrayList<>();
        int x = 0;//左括号位置
        int y = 0;//未匹配括号数量
        for (int i = 0; i < itemStr.length(); i++) {
            if (itemStr.charAt(i) == '(') {
                y++;
                if (y == 1) {
                    x = i;
                }
            } else if (itemStr.charAt(i) == ')') {
                y--;
                if (y == 0) {
                    String s = itemStr.substring(x, i + 1);
                    strings.add(s);
                }
            }
        }
        return strings;
    }
}
