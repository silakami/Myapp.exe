import java.util.Stack;

public class CheckResult {
    /**
     * 采用后缀表达式计算生成的式子,具体思路参照逆波兰式算法
     * @param str   题目
     * @return
     */
    public String Calc(String str){
        GenerateTitle gen = new GenerateTitle();
        char[] title = str.toCharArray();
        Stack<Character> operator = new Stack<Character>();         //符号栈
        operator.push('k');                          //压入标志位'k'
        Stack<String> number = new Stack<String>();              //操作数栈
        number.push("q");                            //压入标志位"q"
        String molecule1, molecule2, denominator1, denominator2;
        String molecule, midel, denominator;
        char operating;
        String[] result;
        String[] finResult = new String[2];
        int judgeXie, judgeFen, divisor;
        outer: for(int i = 0;i < title.length;i++){
            judgeXie = 0;                                   //初始化'/'标志
            judgeFen = 0;                                   //初始化'\''标志
            if(title[i] == '(')
                //若从题目取出"("，则入运算符栈
                operator.push(title[i]);
            else if(title[i] == ')'){
                /*若从题目取出")"，则从操作数栈取出两个操作数，并从运算符栈取出一个运算符，并进行运算
                *运算完的结果重新压入操作数栈，重复以上过程，直到遇到第一个"(",最后将其出栈
                * 计算时一次取两个数，即为操作数的分子和分母
                */
                while(!operator.peek().equals('(')){
                    denominator2 = number.pop();                    //取第二个操作数分母
                    molecule2 = number.pop();                       //取第二个操作数分子
                    denominator1 = number.pop();                    //取第一个操作数分母
                    molecule1 = number.pop();                       //取第一个操作数分母
                    operating = operator.pop();                     //取操作符
                    result = CalcAndCheck(operating, molecule1, denominator1, molecule2, denominator2);
                    //如果不符合规范（产生负数或除数为0），则返回null，重新生成一个新的算式
                    if(result == null)
                        return null;
                    else{
                        //将计算结果压栈
                        number.push(result[0]);
                        number.push(result[1]);
                    }
                }
                //将左括号出栈
                operator.pop();
            }
            else if(title[i] == '+' || title[i] == '-' || title[i] == '×' || title[i] == '÷'){
                /*若从题目取出操作符，若栈顶操作符优先级高于该操作符，则从操作数栈取出两个操作数，并从运算符栈取出一个
                 *运算符，并进行运算，运算完的结果重新压入操作数栈，重复以上过程，直到操作符栈的栈顶元素优先级小于从题
                 *目取出的运算符的优先级（不含等于），则将该操作符压入栈，否则直接入栈
                 */
                if(operator.peek()!='k' && number.peek()!="q"){             //判断栈是否为空
                    operating = operator.peek();                            //取栈顶元素，但不出栈
                    while(operator.peek()!='k' && number.peek()!="q" && !JudgePriority(operating, title[i])){
                        //做上述操作直到栈空或者操作符优先级高于栈顶操作符
                        denominator2 = number.pop();
                        molecule2 = number.pop();
                        denominator1 = number.pop();
                        molecule1 = number.pop();
                        operating = operator.pop();
                        result = CalcAndCheck(operating, molecule1, denominator1, molecule2, denominator2);
                        if(result == null)
                            return null;
                        else{
                            //将计算结果压栈
                            number.push(result[0]);
                            number.push(result[1]);
                        }
                        operating = operator.peek();
                    }
                }
                //操作符压栈
                operator.push(title[i]);
            }
            else if(title[i] >= '0' && title[i] <= '9'){
                /*取出操作数，将操作数压栈
                *这里的将自然数当作分数来算，分母为1，这样极大地减少了计算的复杂度
                * 需要区分
                * 若为真分数，则molecule代表分子部分,denominator代表分母部分
                * 若为假分数，则molecule代表整数部分，midel代表分子部分，denominator代表分母部分
                 */
                molecule = midel = denominator = String.valueOf(title[i]);      //取第一个数
                i++;
                if(i == title.length){
                    //如果到达算式末尾，则直接压入栈
                    number.push(molecule);
                    number.push("1");                   //这种情况只能为整数，所以分母压入1
                    continue;
                }
                //取完整的操作数
                while(i < title.length && title[i] != ' ' && title[i] != ')'){
                    if(title[i] == '/') {
                        //遇到'/'，则为分母，取分母部分
                        judgeXie = 1;
                        i++;
                        denominator = String.valueOf(title[i]);     //取分母的第一位
                        if(i == title.length - 1){      //到达算式末尾
                            i--;
                            if(judgeFen == 1){
                                //判断是否为真分数，如果是，则将需要将带分数转化为假分数，再压栈
                                number.push(String.valueOf(Integer.valueOf(molecule) * Integer.valueOf(denominator) + Integer.valueOf(midel)));
                            }
                            else
                                number.push(molecule);
                            number.push(denominator);
                            break outer;                //下一个循环
                        }
                        i++;
                        continue;
                    }
                    if(title[i] == '\''){
                        //遇到'\''，即为带分数，则取分子部分
                        judgeFen = 1;
                        judgeXie = 2;
                        i++;
                        midel = String.valueOf(title[i]);
                        i++;
                        continue;
                    }
                    if(judgeXie == 0)
                        molecule = molecule + String.valueOf(title[i]);
                    else if(judgeXie == 1)
                        denominator = denominator + String.valueOf(title[i]);
                    else if(judgeFen == 1)
                        midel = midel + String.valueOf(title[i]);
                    i++;
                }
                if(judgeXie == 1){
                    i--;
                    if(judgeFen == 1) {
                        number.push(String.valueOf(Integer.valueOf(molecule) * Integer.valueOf(denominator) + Integer.valueOf(midel)));
                    }
                    else
                        number.push(molecule);
                    number.push(denominator);
                }
                else{
                    i--;
                    number.push(molecule);
                    number.push("1");
                }
            }
        }
        //计算最终结果
        while(operator.peek()!='k'){
            denominator2 = number.pop();
            molecule2 = number.pop();
            denominator1 = number.pop();
            molecule1 = number.pop();
            operating = operator.pop();
            result = CalcAndCheck(operating, molecule1, denominator1, molecule2, denominator2);
            if(result == null)
                //不符合规范，返回null重新生成新的算式
                return null;
            else{
                //将计算结果压栈
                number.push(result[0]);
                number.push(result[1]);
            }
        }
        finResult[1] = number.pop();                //取最终结果分母
        finResult[0] = number.pop();                //取最终结果分子
        if(finResult[1].equals("1"))                //若分母为1，则为整数
            return finResult[0];
        else if(finResult[0].equals("0"))           //若分子为0，则答案为0
            return "0";
        else{
            divisor = gen.CommonDivisor(Integer.valueOf(finResult[0]), Integer.valueOf(finResult[1]));  //计算最大公约数
            //将结果化简和化成分数，并返回
            return gen.ChangePorper(Integer.valueOf(finResult[0]), Integer.valueOf(finResult[1]), divisor);
        }
    }

    /**
     * 判断符号优先级，只有
     * @param befor     符号栈顶的符号
     * @param after     算式的符号
     * @return
     */
    public boolean JudgePriority(char befor, char after){
        if((befor == '+' || befor == '-') && (after == '×' || after == '÷'))
            return true;
        else if(befor == '(') {
            //'('标志着边界，不能再往左计算
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 对操作数进行运算，具体就是简单的两个分数相运算
     * @param operator  操作符
     * @param mole1     操作数1分子
     * @param demo1     操作数1分母
     * @param mole2     操作数2分子
     * @param demo2     操作数2分母
     * @return
     */
    public String[] CalcAndCheck(char operator, String mole1, String demo1, String mole2, String demo2){
        String[] result = new String[2];
        int m1, m2, d1, d2;
        m1 = Integer.valueOf(mole1);
        m2 = Integer.valueOf(mole2);
        d1 = Integer.valueOf(demo1);
        d2 = Integer.valueOf(demo2);
        if(operator == '+'){
            result[0] = String.valueOf(m1 * d2 + m2 * d1);
            result[1] = String.valueOf(d1 * d2);
        }
        else if(operator == '-'){
            if((m1 * d2 - m2 * d1) < 0)
                //判断在计算过程中是否会产生负数
                return null;
            result[0] = String.valueOf(m1 * d2 - m2 * d1);
            result[1] = String.valueOf(d1 * d2);
        }
        else if(operator == '×'){
            result[0] = String.valueOf(m1 * m2);
            result[1] = String.valueOf(d1 * d2);
        }
        else{
            if(m2 == 0 || d2 == 0)
                //除数是否为0
                return null;
            result[0] = String.valueOf(m1 * d2);
            result[1] = String.valueOf(d1 * m2);
        }
        return result;
    }

}
