import java.math.BigInteger;
import java.util.ArrayList;

public class Judge {
    public boolean isLegal(String s) { //判断字符串是否合法
        if (s.isEmpty()) {
            return false;
        }
        boolean flag = false;//判断是否开始匹配首项
        boolean flag1 = false;//判断是否匹配到符号
        boolean flag2 = true;//判断匹配符号后是否匹配了项
        for (int i = 0; i < s.length(); i++) {
            if (!flag) {
                if (s.charAt(i) == '+' || s.charAt(i) == '-') {
                    flag = true;
                    int j = isItem(s, i + 1);
                    if (j == -1) {
                        return false;
                    }
                    i = j;
                } else if (s.charAt(i) == ' ' || s.charAt(i) == '\t') {
                    continue;
                } else {
                    flag = true;
                    int j = isItem(s, i);
                    if (j == -1) {
                        return false;
                    }
                    i = j;
                }
            } else {
                if (!flag1) {
                    if (s.charAt(i) == ' ' || s.charAt(i) == '\t') {
                        continue;
                    } else if (s.charAt(i) == '+' || s.charAt(i) == '-') {
                        flag1 = true;
                        flag2 = false;
                    } else {
                        return false;
                    }
                } else {
                    if (s.charAt(i) == ' ' || s.charAt(i) == '\t') {
                        continue;
                    } else {
                        int j = isItem(s, i);
                        if (j == -1) {
                            return false;
                        }
                        i = j;
                        flag1 = false;
                        flag2 = true;
                    }
                }
            }
        }
        return (flag && flag2) ? true : false;
    }

    //以下输出-1都代表不合法
    public int isItem(String s, int i) {
        int j;
        boolean flag = false;//是否开始匹配因子
        if (i >= s.length()) {
            return -1;
        }
        for (j = i; j < s.length(); j++) {
            if (!flag) {
                if (s.charAt(j) == ' ' || s.charAt(j) == '\t') {
                    continue;
                } else if (s.charAt(j) == '+' || s.charAt(j) == '-') {
                    int k = isYin(s, j + 1, true);
                    flag = true;
                    if (k == -1) {
                        return -1;
                    }
                    j = k;
                } else {
                    int k = isYin(s, j, true);
                    flag = true;
                    if (k == -1) {
                        return -1;
                    }
                    j = k;
                }
            } else {
                if (s.charAt(j) == ' ' || s.charAt(j) == '\t') {
                    continue;
                } else if (s.charAt(j) == '*') {
                    int k = isYin(s, j + 1, false);
                    if (k == -1) {
                        return -1;
                    }
                    j = k;
                } else {
                    return j - 1;
                }
            }
        }
        return j - 1;
    }

    public int isYin(String s, int i, boolean flag) { //在yin里判断是否表达式，其他都在方法里判断,flag标记是不是第一个因子
        if (i >= s.length()) {
            return -1;
        }
        int j;
        for (j = i; j < s.length(); j++) {
            if (s.charAt(j) == ' ' || s.charAt(j) == '\t') {
                continue;
            } else if (s.charAt(j) == 's' || s.charAt(j) == 'c') {
                return isTri(s, j);
            } else if (s.charAt(j) == '(') {
                return isExp(s, j);
            } else if (s.charAt(j) == 'x') {
                return isxPow(s, j);
            } else if (s.charAt(j) == '+' || s.charAt(j) == '-') {
                if (flag) {
                    if (s.charAt(j + 1) >= '0' && s.charAt(j + 1) <= '9') {
                        return isDigit(s, j + 1);
                    }
                    for (int k = j + 1; k < s.length(); k++) {
                        if (s.charAt(k) != ' ' && s.charAt(k) != '\t') {
                            if (s.charAt(k) == 's' || s.charAt(k) == 'c') {
                                return isTri(s, k);
                            } else if (s.charAt(k) == 'x') {
                                return isxPow(s, k);
                            } else if (s.charAt(k) == '(') {
                                return isExp(s, k);
                            } else {
                                return -1;
                            }
                        }
                    }
                } else {
                    return isDigit(s, j);
                }
            } else if (s.charAt(j) >= '0' && s.charAt(j) <= '9') {
                return isDigit(s, j);
            } else {
                return -1;
            }
        }
        return -1;
    }

    public int isExp(String s, int i) {
        int num = 0;
        if (s.charAt(i) != '(') {
            return -1;
        }
        for (int k = i; k < s.length(); k++) {
            if (s.charAt(k) == '(') {
                num++;
            } else if (s.charAt(k) == ')') {
                num--;
                if (num == 0) {
                    String s1 = s.substring(i + 1, k);
                    if (!isLegal(s1)) {
                        return -1;
                    }
                    return k;
                }
            }
        }
        return -1;
    }

    public int isDigit(String s, int i) {
        int j;
        boolean flag = false;//是否开始匹配数字
        boolean flagNum = false;
        for (j = i; j < s.length(); j++) {
            if (!flag) {
                if (s.charAt(j) == '+' || s.charAt(j) == '-') {
                    flag = true;
                } else if (s.charAt(j) == ' ' || s.charAt(j) == '\t') {
                    continue;
                } else if (s.charAt(j) >= '0' && s.charAt(j) <= '9') {
                    flag = true;
                    flagNum = true;
                } else {
                    return -1;
                }
            } else {
                if (s.charAt(j) >= '0' && s.charAt(j) <= '9') {
                    flagNum = true;
                } else {
                    break;
                }
            }
        }
        if (!flagNum) {
            return -1;
        } else {
            return j - 1;
        }
    }

    public int isTri(String s, int i) {
        int j = i;
        if (!(s.startsWith("sin", j) || s.startsWith("cos", j))) {
            return -1;
        }
        boolean flagFactor = false;
        for (j = i + 3; j < s.length(); j++) {
            if (!flagFactor) {
                if (s.charAt(j) == ' ' || s.charAt(j) == '\t') {
                    continue;
                } else if (s.charAt(j) == '(') {
                    int k = isExp(s, j);
                    if (k == -1) {
                        return -1;
                    } else {
                        String s1 = s.substring(j + 1, k);
                        if (!isSingleYin(s1)) {
                            return -1;
                        }
                        j = k;
                        flagFactor = true;
                    }
                } else {
                    return -1;
                }
            } else {
                return isIndex(s, j);
            }
        }
        return flagFactor ? j - 1 : -1;
    }

    boolean isSingleYin(String s1) {
        Process process = new Process();
        String s = s1.trim();
        ArrayList<Item> items = process.getItems(s);
        if (items.size() == 0) {  //item 为0
            return true;
        } else if (items.size() > 1) {
            return false;
        }
        int l = s.length();
        if (isxPow(s, 0) == l - 1 || isDigit(s, 0) == l - 1
                || isTri(s, 0) == l - 1 || isExp(s, 0) == l - 1) {
            return true;
        } else {
            return false;
        }
    }

    public int isxPow(String s, int i) {
        if (s.charAt(i) == 'x') {
            return isIndex(s, i + 1);
        } else {
            return -1;
        }
    }

    int isIndex(String s, int i) {
        if (i >= s.length()) {
            return s.length() - 1;
        }
        int j;
        boolean flag = false;//是否匹配到**；
        boolean flag1 = false;//是否匹配到后面的数字
        for (j = i; j < s.length(); j++) {
            if (!flag) {
                if (s.charAt(j) == ' ' || s.charAt(j) == '\t') {
                    continue;
                } else if (s.startsWith("**", j)) {
                    flag = true;
                    j = j + 1;
                } else {
                    return j - 1;
                }
            } else {
                int k = isDigit(s, j);
                if (k == -1) {
                    return -1;
                } else {
                    flag1 = true;
                    String s1 = s.substring(j, k + 1);
                    s1 = s1.trim();
                    BigInteger index = new BigInteger(s1);
                    if (index.abs().compareTo(BigInteger.valueOf(50)) > 0) {
                        return -1;//限制指数的范围
                    } else {
                        return k;
                    }
                }
            }
        }
        if (flag && !flag1) {
            return -1;
        } else {
            return j - 1;
        }
    }
}
