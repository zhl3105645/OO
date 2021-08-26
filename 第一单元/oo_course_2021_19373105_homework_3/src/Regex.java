public class Regex {
    //预处理完空白字符和多余的加减号 //不包含表达式因子的正则表达式
    private final String addSub = "[+-]";
    private final String integer = "(" + addSub + "?" + "\\d+)";
    private final String index = "\\*\\*" + integer;
    private final String pow = "(x" + "(" + index + ")?)";
    private final String consYin = integer;
    private final String yin = "((" + pow + ")|(" + consYin + "))";

    public String getPow() {
        return pow;
    }

    public String getYin() {
        return yin;
    }
}