import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.nextLine();
        line = line.replaceAll("\\s*", "");//去除空白字符
        line = line.replaceAll("\\+\\+", "+");
        line = line.replaceAll("(\\+-)|(-\\+)", "-");
        line = line.replaceAll("--", "+");
        line = line.replaceAll("\\+\\+", "+");
        line = line.replaceAll("(\\+-)|(-\\+)", "-");
        line = line.replaceAll("--", "+");
        Process process = new Process();

        String bianYin = "([+-]?x(\\*{2}[+-]?\\d+)?)";
        String chanYin = "([+-]?\\d+)";
        String yin = "(" + bianYin + "|" + chanYin + ")";
        String xiang = "(" + "[+-]?" + yin + ")" + "(" + "\\*" + yin + ")*";

        Pattern p = Pattern.compile(xiang);
        Matcher m = p.matcher(line);

        Many many = new Many();
        while (m.find()) {
            String singleStr = m.group();
            //System.out.println(singleStr);
            Single single = process.getSingle(singleStr);
            many.addSingle(single);
        }
        String manyDaoStr = many.manyDao();
        System.out.println(manyDaoStr);
    }
}
