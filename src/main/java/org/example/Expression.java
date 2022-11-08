package org.example;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Expression {
    private String strExpression, result;
    private int index;
    private char ch;
    private Map variables;

    private void currentCh()
    {
        this.ch = ++this.index < this.strExpression.length() ? this.strExpression.charAt(this.index) : '@';
        //System.out.println(ch);
    }
    private void previousCh()
    {
        this.ch = --this.index >= 0 ? this.strExpression.charAt(this.index) : '@';
        //System.out.println(ch);
    }
    public Expression (String str)
    {
        this.strExpression = str;
        this.index =-1;
        variables = new HashMap<>();
    }

    public void setVariable(char key, double value)
    {
        variables.put(key,value);
    }

    public String calculateExpression ()
    {
        return calculateExpression(null, true);
    }
    public String calculateExpression (String st, boolean clearMap)
    {
        if (st!=null)
            strExpression = st;
        if (clearMap)
            variables.clear();
        if (strExpression != null && strExpression!="")
        {
            if(checkExpression(null))
            {
                this.result = "";
                this.index = -1;
                //if (checkExpression())
                //{
                //    double meanExpression = calculationRecursion();
                //    if (!this.result.equals("Error in calculation!"))
                //        this.result = Double.toString(meanExpression);
                //}
                double meanExpression = 0;
                currentCh();
                meanExpression = secondPriority();

                //while (this.index < this.strExpression.length()  && !this.result.equals("Error in calculation!"))
                //{
                //    currentCh();
                //System.out.println(ch);

                //    meanExpression += secondPriority();
                //}
                if (this.result.equals(""))
                {
                    //System.out.println(meanExpression);
                    //BigDecimal b = new BigDecimal(meanExpression, MathContext.DECIMAL64);
                    //BigDecimal b = BigDecimal.valueOf(meanExpression);
                    //System.out.println("------");
                    //System.out.println(b.toString());
                    //System.out.println("------");
                    //result = b.toString();
                    result = String.format("%.6f", meanExpression);
                    //result = Double.toString(meanExpression);
                    result = result.replaceAll(",", ".");
                    //System.out.println("------");
                    //System.out.println(result);
                    //System.out.println("------");
                    double res = Double.parseDouble(result);
                    result = Double.toString(res);
                    //result = BigDecimal.toString(meanExpression);
                }
            }
            else
                result = "Syntax error in the expression!";
        }
        else
            result = "The expression is not set!";
        return this.result;
    }
    public boolean checkExpression(String str)
    {
        if (str!=null)
            strExpression = str;
        boolean res = true;
        int numberPoint = 0;
        int numberStaples = 0;
        currentCh();
        while (ch!='@' && res)
        {
            if (ch =='.')
            {
                numberPoint++;
                currentCh();
                if (ch >= '9' || ch <= '0' || numberPoint>1)
                    res = false;
                else
                    previousCh();
            }
            else if (ch >= 'a' && ch <= 'z')
            {
                numberPoint=0;
                int startInd = this.index;
                while (ch >= 'a' && ch <= 'z' && this.index < startInd + 4)
                    currentCh();
                String function = this.strExpression.substring(startInd, this.index);
                if (function.equals("sqrt"))
                {
                    if (!(ch <= '9' && ch >= '0') && !(ch <= 'z' && ch >= 'a') && ch!='(' && ch!='-')
                        res = false;
                    else
                        previousCh();
                }
                else
                {
                    if (function.length() == 4)
                        previousCh();
                    function = this.strExpression.substring(startInd, this.index);
                    if (function.equals("sin") || function.equals("cos") || function.equals("tan") || function.equals("abs"))
                    {
                        if (!(ch <= '9' && ch >= '0') && !(ch <= 'z' && ch >= 'a') && ch!='(' && ch!='-')
                            res = false;
                        else
                            previousCh();
                    }
                    else
                    {
                        this.index = startInd;
                        ch = this.strExpression.charAt(this.index);
                        currentCh();
                        if (ch == '.')
                            res = false;
                        else
                            previousCh();
                    }
                }

            }
            else if (ch == '+' || ch == '-' || ch == '*' || ch == '/' || ch == '^' || ch == '(')
            {
                numberPoint=0;
                if (ch == '(')
                    if (numberStaples>=0)
                        numberStaples++;
                    else
                        res = false;
                currentCh();
                if (!(ch <= '9' && ch >= '0') && !(ch <= 'z' && ch >= 'a') && ch!='(' && ch!='-')
                    res = false;
                else
                    previousCh();
            }
            else if (ch == ')')
            {
                numberPoint=0;
                if (numberStaples>0)
                {
                    numberStaples--;
                    currentCh();
                    if (ch == '.')
                        res = false;
                    else
                        previousCh();
                }
                else
                    res = false;
            }
            else if (ch < '0' || ch > '9')
                res = false;
            currentCh();
        }
        if (numberStaples!=0)
            res = false;
        return res;
    }
    private double firstPriority()
    {
        double x = calculation();
        for (;;)
        {
            if (ch == '*')
            {
                currentCh();
                x *= calculation();
                //BigDecimal b = BigDecimal.valueOf(firstPriority());
                //double d = b.doubleValue();
                //x *= d;
            }
            else
                if (ch == '/')
                {
                    currentCh();
                    double divider = calculation();
                    //double divider = calculation();
                    if (divider!=0)
                        x /= divider;
                    else
                        result = "Error in calculation (division by zero)!";
                }
                else
                    return x;
        }
    }
    private double secondPriority()
    {
        double x = firstPriority();
        for (;;)
        {
            if (ch == '+')
            {
                currentCh();
                x += firstPriority();
                //BigDecimal b = BigDecimal.valueOf(firstPriority());
                //double d = b.doubleValue();
                //x += d;
            }
            else
                if (ch == '-')
                {
                    currentCh();
                    x -= firstPriority();
                    //BigDecimal b = BigDecimal.valueOf(firstPriority());
                    //double d = b.doubleValue();
                    //x -= d;

                }
                else
                    return x;
        }
    }
//    private double calculationRecursion()
//    {
//        double x = calculation();
//        return x;
//    }
    private double calculation()
    {
        //System.out.println(ch);
        //System.out.println(index);
        if (ch == '+')
        {
            currentCh();
            //return calculation();
            return secondPriority();
        }
        if (ch == '-')
        {
            currentCh();
            //return (-1)*calculation();
            //double res = secondPriority();
            //return (-1.0)*res;
            return -secondPriority();
        }
        Double res = null;
        if (ch == '(')
        {
            currentCh();
            res = secondPriority();
            if (ch != ')')
            {
                result = "Error in calculation (Missing ')' )!";
            }
            else
            {
                currentCh();
            }


//            else
//            {
//                currentCh();
//                if (ch >= '0' && ch <= '9' || ch == '(' || ch >= 'a' && ch <= 'z')
//                {
//                    //System.out.println("mi tut");
//                    //currentCh();
//                    //res *= calculation();
//                    //res *= secondPriority();
//                    res *= calculation();
//                }
//            }




        }
        else
            if ((ch >= '0' && ch <= '9'))
            //if ((ch >= '0' && ch <= '9') || ch =='.')
            {
                //System.out.println("mi tut");
                int startInd = this.index;
                int numberpoint = 0;
                while ((ch >= '0' && ch <= '9') || ch =='.')
                {
                    if (ch =='.')
                        numberpoint++;
                    currentCh();
                }
                if (numberpoint<=1)
                    res = Double.parseDouble(this.strExpression.substring(startInd, this.index));
                else
                {
                    result = "Error in calculation (incorrect number format)!";
                    res = 0.0;
                }
                //System.out.print("proneg res ");
                //System.out.println(res);



//                if (ch >= 'a' && ch <= 'z' || ch == '(')
//                {
//                    //currentCh();
//                    res *= calculation();
//                    //res *= secondPriority();
//                }



                //else if (ch =='^')
                //{
                //    res = Math.pow(res,calculation());
                //}
            }
            else
                if (ch >= 'a' && ch <= 'z')
                {
                    int startInd = this.index;
                    while (ch >= 'a' && ch <= 'z' && this.index < startInd + 4)
                        currentCh();
                    String function = this.strExpression.substring(startInd, this.index);
                    if (function.equals("sqrt"))
                    {
                        res = Math.sqrt(calculation());
                    }
                    else
                    {
                        if (function.length() == 4)
                            previousCh();
                        function = this.strExpression.substring(startInd, this.index);
                        //System.out.println("--------");
                        //System.out.println(function);
                        //System.out.println(ch);
                        //System.out.println("--------");
                        if (function.equals("sin"))
                            res = Math.sin(calculation());
                        else if (function.equals("cos"))
                            res = Math.cos(calculation());
                        else if (function.equals("tan"))
                            res = Math.tan(calculation());
                        else if (function.equals("abs"))
                            res = Math.abs(calculation());
                        else {
                            this.index = startInd;
                            ch = this.strExpression.charAt(this.index);
                            double currentVariable = 0.0;
                            //Scanner in = new Scanner(System.in);
                            //currentVariable = in.nextDouble();

                            if (variables.containsKey(ch)) {
                                currentVariable = (double) variables.get(ch);
                                //System.out.print("Value "+ch+" -> ");
                                //System.out.println(currentVariable);
                            } else {
                                Scanner in = new Scanner(System.in);
                                System.out.print("Value " + ch + " -> ");
                                currentVariable = in.nextDouble();
                                setVariable(ch, currentVariable);
                                //variables.put(ch,currentVariable);
                            }
                            //System.out.print(currentVariable);
                            res = currentVariable;
                            currentCh();


//                                                if (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch == '(')
//                                                {
//                                                    //currentCh();
//                                                    res *= calculation();
//                                                }


                            //else if (ch =='^')
                            //{
                            //    res = Math.pow(res,calculation());
                            //}
                        }
                    }
                }
        if (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch == '(' || ch == '.' || ch == ',')
        {
            //currentCh();
            if (ch == '.' || ch == ',')
            {
                result = "Error in calculation (incorrect expression format)!";
                res = 0.0;
            }
            else
                res *= calculation();
        }
        if (ch =='^')
        {
            currentCh();
            double degree = calculation();
            if (res<0 && degree <1)
            {
                result = "Error in calculation (it is impossible to raise to a degree)!";
                res = 0.0;
            }
            else
                res = Math.pow(res,calculation());
        }
        if (res == null)
        {
            result = "Error in calculation (incorrect expression format)!";
            res = 0.0;
        }
        return res;
    }
}
