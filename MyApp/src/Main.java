import java.util.*;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        GenerateTitle generateTitle = new GenerateTitle();
        CheckResult checkResult = new CheckResult();
        FileOperation fileOperation = new FileOperation();
        String fileHandle;
        String[] fileSpit;
        System.out.println("-------------------------------------------------------------------");
        System.out.println("小学生四则运算生成器");
        System.out.println("请按格式输入来选择以下功能:");
        System.out.println("Myapp.exe -n x -r y     x为生成题目个数,y为题目中自然数，真分数，真分数分母的范围(x和y大于0)");
        System.out.println("题目和答案分别在Exercises.txt和Answers.txt中生成");
        System.out.println("               ");
        System.out.println("Myapp.exe -e <exercisefile>.txt -a <answerfile>.txt");
        System.out.println("以上两个txt文件分别为想要判定对错的题目和答案");
        System.out.println("               ");
        System.out.println("-q     退出生成器");
        System.out.println("-------------------------------------------------------------------");
        final String FILEMATCHONE = "(Myapp.exe|myapp.exe)(\\s+(-n))(\\s+\\d+)(\\s+(-r))(\\s+\\d+)";
        final String FILEMATCHTWO = "(Myapp.exe|myapp.exe)(\\s+(-e))(\\s+\\S+)(\\s+(-a))(\\s+\\S+)";
        int judge = 0;
        while(true){
            judge = 0;
            Scanner instruct = new Scanner(System.in);
            fileHandle = instruct.nextLine();
            fileSpit = fileHandle.split("\\s+");
            if(fileHandle.equals("-q")){
                System.out.println("谢谢使用");
                break;
            }
            else if(Pattern.matches(FILEMATCHONE, fileHandle)){
                //long startTime = System.currentTimeMillis(); //获取开始时间
                judge = generateTitle.creatTitle(Integer.valueOf(fileSpit[2]), Integer.valueOf(fileSpit[4]));
                if(judge == 0)
                    System.out.println("题目答案生成成功");
                else if(judge == 1) {
                    System.out.println("参数错误，n需要大于0");
                    System.out.println("请重新输入，格式为：");
                    System.out.println("Myapp.exe -n x -r y     x为生成题目个数,y为题目中自然数，真分数，真分数分母的范围(x和y大于0)");
                }
                else {
                    System.out.println("参数错误，r需要大于0");
                    System.out.println("请重新输入，格式为：");
                    System.out.println("Myapp.exe -n x -r y     x为生成题目个数,y为题目中自然数，真分数，真分数分母的范围(x和y大于0)");
                }
                //long endTime = System.currentTimeMillis(); //获取结束时间
                //System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
            }
            else if(Pattern.matches(FILEMATCHTWO, fileHandle)){
                judge = fileOperation.FReader(fileSpit[2], fileSpit[4]);
                if(judge == 1)
                    System.out.println("答案校对完毕，成绩已在Grade.txt出生成");
                else{
                    System.out.println("请重新输入正确的文件路径");
                }
            }
            else {
                System.out.println("参数输入错误,请按照格式输入");
                System.out.println("-------------------------------------------------------------------");
                System.out.println("请按格式输入来选择以下功能:");
                System.out.println("Myapp.exe -n x -r y     x为生成题目个数,y为题目中自然数，真分数，真分数分母的范围(x和y大于0)");
                System.out.println("题目和答案分别在Exercises.txt和Answers.txt中生成");
                System.out.println("               ");
                System.out.println("Myapp.exe -e <exercisefile>.txt -a <answerfile>.txt");
                System.out.println("以上两个txt文件分别为想要判定对错的题目和答案");
                System.out.println("               ");
                System.out.println("-q     退出生成器");
            }
            System.out.println("-------------------------------------------------------------------");
            System.out.println("请输入命令：");
        }
    }
}
