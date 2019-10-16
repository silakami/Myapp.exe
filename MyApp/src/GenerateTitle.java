import java.util.Random;

public class GenerateTitle {
    /**
     * 随机生成题目
     * @param n     生成题目的数量
     * @param r     自然数、真分数和真分数分母的范围
     * @return
     */
    public int creatTitle(int n, int r){
        //检测输入
        if(n <= 0)
            return 1;
        else if(r <= 0)
            return 2;
        CheckResult check = new CheckResult();
        FileOperation fileOperation = new FileOperation();
        String[] title = new String[n];                         //存放题目
        String[] answer = new String[n];                        //存放答案
        String[] operator = new String[3];                      //存放操作符
        String[] num = new String[4];                           //存放操作数
        //String[] checkCop;            标志重复
        //int  k, w;
        int i, j ,operatorNum, intOrFloat, operationNumber;     //operatorNum是运算符个数，intOrFloat是指定生成真分数还是自然数,operationNumber是生成操作数的个数
        Random ra = new Random();
        String checkOut, checkCopy,judgement;
        /*检测重复用
        if(n < (4*r*(r-1)*(r-1)*12*6/2))
            judgement = "yes";
        else
            judgement = "no";
            */
        for(i = 0;i < n;i++){
            operatorNum = ra.nextInt(3) + 1;    //随机生成操作符个数
            operationNumber = operatorNum + 1;          //操作数个数要比操作符个数多1
            operator = creatOperator(operatorNum);
            for(j = 0;j < operationNumber;j++){
                intOrFloat = ra.nextInt(2);     //随机生成自然数还是浮点数
                if(intOrFloat == 0)
                    num[j] = creatInt(r);               //生成自然数
                else
                    num[j] = creatFloat(r);             //生成浮点数
            }
            title[i] = Cover(num,operator,operatorNum); //将操作符，操作数组装成算式，并且加上括号
            checkOut = check.Calc(title[i]);  //极端答案
            if(checkOut == null)
                //遇到负数和符号后面为0的情况
                i--;
            else{
                answer[i] = Integer.toString(i + 1) + ". " + checkOut;
                title[i] = Integer.toString(i + 1) + ". " + title[i];
            }
            /*
            else {
                //检查重复的算式
                checkCopy = "no";
                if(judgement == "yes" && i != 0)
                    for(k = 0;k < i;k++){
                        checkCop = answer[k].split(Integer.toString(k+1)+". ");
                        if(checkCop[1].equals(checkOut)){
                        //减少检测数量，检测答案相同的式子
                            checkCop = title[k].split(Integer.toString(k+1)+". ");
                            w = 0;
                            //检测长度相同的式子
                            if(!checkCop[1].contains(num[0]) && checkCop[1].split(" ").length == (operationNumber + operatorNum))
                                continue;
                            while(w < operatorNum){
                            //检测式子内部内容
                                if(!checkCop[1].contains(num[w+1]))
                                    break;
                                else if(!checkCop[1].contains(operator[w]))
                                    break;
                                w++;
                            }
                            checkCopy = "yes";
                            break;
                        }
                        else
                            checkCopy = "no";
                    }
                if(checkCopy == "no") {
                    answer[i] = Integer.toString(i + 1) + ". " + checkOut;
                    title[i] = Integer.toString(i + 1) + ". " + title[i];
                }
                else
                    i--;
            }*/
        }
        fileOperation.FWriter(title, answer);   //写入文件
        System.out.println("ok");
        return 0;
    }

    /**
     * 随机生成操作符
     * @param num   操作符个数
     * @return
     */
    public String[] creatOperator(int num){
        String[] str = new String[num];
        Random ra = new Random();
        int i,j;
        for(i = 0;i < num;i++){
            j = ra.nextInt(4);
            switch (j){
                case 0:
                    str[i] = "+";
                    break;
                case 1:
                    str[i] = "-";
                    break;
                case 2:
                    str[i] = "×";
                    break;
                case 3:
                    str[i] = "÷";
                    break;
                default:
                    System.out.println("error");
                    break;
            }
        }
        return str;
    }

    /**
     * 随机生成自然数
     * @param r     自然数范围
     * @return      生成的自然数
     */
    public String creatInt(int r){
        int i;
        String naturalNum;
        Random ra = new Random();
        i = ra.nextInt(r);
        naturalNum = Integer.toString(i);
        return naturalNum;
    }

    /**
     * 随机生成真分数
     * @param r
     * @return
     */
    public String creatFloat(int r){
        int molecule, denominator, commentFactor;
        String[] str = new String[3];
        String fraction;
        Random ra = new Random();
        //随机生成分母
        denominator = ra.nextInt(r) + 2;
        while(denominator >= r){
            denominator = ra.nextInt(r) + 2;    //分母不能大于等于r，也不能为0
        }
        molecule = ra.nextInt(denominator*denominator) + 1;     //生成分子，算出分子的范围
        while((molecule/denominator) >= r) {
            molecule = ra.nextInt(denominator*denominator) + 1;
        }
        if(molecule % denominator == 0)
            //保证分子和分母不相同
            molecule = molecule - 1;
        commentFactor = CommonDivisor(molecule,denominator);    //对算出分子分母的最大公因数，并进行约分
        if(commentFactor != 0) {
            molecule = molecule / commentFactor;
            denominator = denominator / commentFactor;
        }
        //判断是生成真分数还是带分数
        if(molecule < denominator) {
            str[1] = Integer.toString(molecule);
            str[2] = Integer.toString(denominator);
            fraction = str[1] + "/" + str[2];
        }
        else
            fraction = ChangeFloat(molecule, denominator);
        return fraction;
    }

    /**
     * 生成带分数
     * @param m    分子
     * @param n    分母
     * @return
     */
    public String ChangeFloat(int m, int n){
        String[] str = new String[3];
        String x;
        int midel, commentFactor;
        commentFactor = CommonDivisor(m,n);
        if(commentFactor != 0) {
            m = m / commentFactor;
            n = n / commentFactor;
        }
        midel = m / n;
        str[0] = Integer.toString(midel);
        str[1] = Integer.toString(m - midel*n);
        str[2] = Integer.toString(n);
        x = str[0] + "'" + str[1] + "/" + str[2];
        return x;
    }

    /**
     * 计算最终结果，并进行约分化简
     * @param m     分子
     * @param d     分母
     * @param divisor   最大公因数
     * @return
     */
    public String ChangePorper(int m, int d, int divisor){
        int midel = 0;
        if(divisor != 1){
            m = m / divisor;
            d = d / divisor;
        }
        if(d == 1)
            //结果是整数
            return Integer.toString(m);
        if (m < d)
            //结果是真分数
            return Integer.toString(m) + "/" + Integer.toString(d);
        else if(m == d)
            return "1";
        else{
            //结果是带分数
            midel = m / d;
            return Integer.toString(midel) + "'" + Integer.toString(m - midel*d) + "/" + Integer.toString(d);
        }
    }

    /**
     * 获取最大公因数，使用辗转相除法
     * @param m 分母
     * @param n 分子
     * @return
     */
    public int CommonDivisor(int m, int n){
        int comment;
        if(n == 1)
            return 1;
        if(m < n){
            int temp = m;
            m = n;
            n = temp;
        }
        if(n == 0)
            return 0;
        comment = m % n;
        while(comment > 0){
            m = n;
            n = comment;
            comment = m % n;
        }
        return n;
    }

    /**
     * 生成括号
     * @param num   操作数数组
     * @param op    操作符数组
     * @param operatorNum   操作数个数
     * @return
     */
    public String Cover(String[] num, String[] op,int operatorNum){
        String title = null;
        int i, j, k, judge, lPoint, rPoint,midel,judge_left,judge_right;
        Random ra = new Random();
        if(operatorNum == 1)
            title = num[0] + " " +op[0] + " " + num[1];     //只有一个操作符，不能生成括号
        else{
            k = ra.nextInt(operatorNum);    //随机生成括号数量
            if(k >= operatorNum)            //括号数量需要小于等于操作符数量减1，否则括号生成位置会出错
                k = ra.nextInt(operatorNum);
            if(k == 0) {
                //不生成括号，直接组装算式
                title = num[0];
                for (i = 1; i < operatorNum + 1; i++)
                    title = title + " " + op[i - 1] + " " + num[i];
            }
            else{
                //采用两个数组，长度为操作数个数，用来标志左右括号的位置
                int[] leftBracket = new int[operatorNum + 1];           //左括号，元素值为该位置左括号数量
                int[] rightBracket = new int[operatorNum + 1];          //右括号，元素值为该位置右括号数量
                for(i = 0;i < operatorNum+1;i++) {
                    //需要赋初值
                    leftBracket[i] = 0;
                    rightBracket[i] = 0;
                }
                for(i = 0;i < k;i++){                       //生成括号
                    lPoint = ra.nextInt(operatorNum);       //随机生成左括号位置
                    judge = 0;                              //标志作用
                    //判断该位置是否有左括号
                    if(leftBracket[lPoint] == 0) {
                        midel = 0;                           //标志该位置没有左括号
                        leftBracket[lPoint]++;
                    }
                    else{
                        midel = 1;                          //标志该位置存在左括号
                        leftBracket[lPoint]++;
                    }
                    rPoint = ra.nextInt(operatorNum) + 1;   //随机生成右括号
                    while(rPoint <= lPoint || (lPoint == 0 && rPoint == operatorNum))
                        //该右括号不能在左括号左边，且不能和左括号在同一位置
                        rPoint = ra.nextInt(operatorNum) + 1;
                    if(midel == 0){                     //该位置上没有左括号
                        if(leftBracket[rPoint] != 0) {  //判断在右括号的位置上是否存在左括号
                            while (leftBracket[rPoint] != 0 || (lPoint == 0 && rPoint == operatorNum) || rPoint <= lPoint) {
                                //随机再产生右括号，若当跳出循环前都没有合适的右括号，则去掉左括号
                                rPoint = ra.nextInt(operatorNum) + 1;
                                judge++;                                    //防止出现死循环
                                if(judge == operatorNum)                    //跳出循环
                                    break;
                            }
                            if(rightBracket[lPoint] != 0 || leftBracket[rPoint] != 0 || judge == operatorNum) {
                                //避免先生成左括号再生成右括号或者反过来生成的情况，即左括号位置有右括号或者右括号位置有左括号，如(3+(4)+5+6)
                                leftBracket[lPoint]--;          //去掉该左括号
                                continue;
                            }
                            rightBracket[rPoint]++;             //添加右括号
                        }
                        else
                            rightBracket[rPoint]++;             //添加右括号
                    }
                    else{                   //在生成左括号位置已经有左括号
                        if(rightBracket[rPoint] != 0) {         //右括号位置已经存在右括号，这种情况不合法，需要重新生成，如((3+4))+5
                            while (rightBracket[rPoint] != 0 || (lPoint == 0 && rPoint == operatorNum) || rPoint <= lPoint) {
                                rPoint = ra.nextInt(operatorNum) + 1;
                                judge++;                        //防止死循环
                                if(judge == operatorNum)
                                    break;
                            }
                            if(rightBracket[lPoint] != 0 || leftBracket[rPoint] != 0 || judge == operatorNum) {
                                leftBracket[lPoint]--;
                                continue;
                            }
                            rightBracket[rPoint]++;
                        }
                        else
                            rightBracket[rPoint]++;
                    }
                    judge_left = 0;
                    judge_right = 0;
                    //由于为了避免增加复杂情况来进行判断，这里采用了从内向外的括号生成方法
                    //这样减少了括号嵌套的概率，但使代码变简单
                    for(j = lPoint;j <= rPoint;j++){
                        if(leftBracket[j]!=0)
                            //遇到左括号，加该位置左括号个数
                            judge_left+=leftBracket[j];
                        if(rightBracket[j]!=0)
                            //加右括号
                            judge_right+=rightBracket[j];
                    }
                    if(judge_left!=judge_right){
                        //如果左括号数量不等于右括号，则不生成括号
                        rightBracket[rPoint]--;
                        leftBracket[lPoint]--;
                        continue;
                    }
                    //添加括号
                    num[lPoint] = "(" + num[lPoint];
                    num[rPoint] = num[rPoint] + ")";
                }
            }
            title = num[0];
            for(i = 1;i < operatorNum+1;i++)
                title = title + " " + op[i - 1] + " " + num[i];
            return title;
        }
        return title;
    }

}
