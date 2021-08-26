public class Regex {
    //预处理完空白字符和多余的加减号 //不包含表达式因子的正则表达式
    private final String addSub = "[+-]";
    private final String integer = "(" + addSub + "?" + "\\d+)";
    private final String index = "\\*\\*" + integer;
    private final String sin = "(sinx" + "(" + index + ")?)";
    private final String cos = "(cosx" + "(" + index + ")?)";
    private final String triFun = "((" + sin + ")|(" + cos + "))";
    private final String pow = "(x" + "(" + index + ")?)";
    private final String consYin = integer;
    private final String varsYin = "((" + triFun + ")|(" + pow + "))";
    private final String yin = "((" + varsYin + ")|(" + consYin + "))";
    private final String item = "(" + addSub + "?" + yin + "(\\*" + yin + ")*)";
    private final String exp = "(" + addSub + "?" + item + "(\\*" + item + ")*";

    public String getSin() {
        return sin;
    }

    public String getCos() {
        return cos;
    }

    public String getPow() {
        return pow;
    }

    public String getYin() {
        return yin;
    }
}
