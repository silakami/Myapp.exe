import java.io.*;

public class FileOperation {
    /**
     * 写入文件方法，将生成的题目和答案写入文本文件
     * @param title     题目数组
     * @param answer    答案数组
     */
    public void FWriter(String[] title,String[] answer){
        String titleFile = "src\\Exercises.txt";
        String answerFile = "src\\Answers.txt";
        int length = title.length;
        int i = 0;
        try{
            //打开文件输出流
            BufferedWriter tFile = new BufferedWriter(new FileWriter(new File(titleFile)));
            BufferedWriter aFile = new BufferedWriter(new FileWriter(new File(answerFile)));
            while(i < length){
                //用字符流输出
                tFile.write(title[i]);
                aFile.write(answer[i]);
                tFile.newLine();
                aFile.newLine();
                i++;
            }
            tFile.close();
            aFile.close();
        }catch(Exception ex){
            ex.getMessage();
        }
    }

    /**
     * 读取文件方法，读取用户输入的题目和答案文件并进行校对，将成绩输入到Grade文件
     * @param titlePath     题目路径
     * @param answerPath    答案路径
     * @return
     */
    public int FReader(String titlePath, String answerPath){
        String title = null;
        String answer = null;
        String correct = "Correct:(";
        String wrong = "Wrong:(";
        String[] str = new String[2];
        int i = 1,correctNum = 0;
        try{
            //读取文件内容
            BufferedReader titleReader = new BufferedReader(new FileReader(new File(titlePath)));
            BufferedReader answerReader = new BufferedReader(new FileReader(new File(answerPath)));
            while((title = titleReader.readLine()) != null){
                //每读取一行，就进行判断，并进行统计对错数量
                answer = answerReader.readLine();
                if(CheckAnswer(title, answer)){
                    correctNum++;
                    if(correct.equals("Correct:("))
                        correct = correct + Integer.toString(i);
                    else
                        correct = correct + ", " + Integer.toString(i);
                }
                else
                    if(wrong.equals("Wrong:("))
                        wrong = wrong + Integer.toString(i);
                    else
                        wrong = wrong + ", " + Integer.toString(i);
                i++;
            }
            titleReader.close();
            answerReader.close();
            str = correct.split(":");
            correct = str[0] + ":" + Integer.toString(correctNum) + str[1] + ")";
            str = wrong.split(":");
            wrong = str[0] + ":" + Integer.toString(i - correctNum - 1) + str[1] + ")";
            //将成绩写入Grade文档
            BufferedWriter gradeFile = new BufferedWriter(new FileWriter(new File("src\\Grade.txt")));
            gradeFile.write(correct);
            gradeFile.newLine();
            gradeFile.write(wrong);
            gradeFile.close();
            return 1;
        }catch(Exception ex){
            ex.getMessage();
            System.out.println("文件路径不正确");
            return 2;
        }
    }

    /**
     * 对答案方法，将计算题目答案并和用户答案对比
     * @param title     题目
     * @param answer    答案
     * @return
     */
    public boolean CheckAnswer(String title, String answer){
        CheckResult check = new CheckResult();
        String[] str;
        str = title.split("\\. ",2);
        title = str[str.length-1];
        str = answer.split("\\. ",2);
        answer = str[str.length-1];
        if(check.Calc(title).equals(answer))
            return true;
        else
            return false;
    }
}
