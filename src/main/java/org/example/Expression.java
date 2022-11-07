package org.example;

import java.util.HashMap;
import java.util.Map;

public class Expression {
    private String strExpression, result;
    private int index, ch;
    private Map<Integer, Double> variables;

    private void currentCh()
    {
        this.ch = ++this.index < this.strExpression.length() ? this.strExpression.charAt(this.index) : -1;
    }
    private void previousCh()
    {
        this.ch = --this.index >= 0 ? this.strExpression.charAt(this.index) : -1;
    }
    public Expression (String str)
    {
        this.strExpression = str;
        this.index =-1;
        variables = new HashMap<>();
    }
    public String calculateExpression ()
    {
        this.result = "";
        this.index =-1;
        if (checkExpression())
        {
            double meanExpression = calculationRecursion();
            if (!this.result.equals("Error in calculation!"))
                this.result = Double.toString(meanExpression);
        }
        return this.result;
    }
    private boolean checkExpression()
    {
        boolean res = true;
        if (true)
            this.result = "Syntax error in the expression!";
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
                return x * calculation();
            }
            else
                if (ch == '/')
                {
                    currentCh();
                    double divider = calculation();
                    if (divider!=0)
                        return x/divider;
                    else
                        result = "Error in calculation!";
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
                return x + firstPriority();
            }
            else
                if (ch == '-')
                {
                    currentCh();
                    return x - firstPriority();
                }
                else
                    return x;
        }
    }
    private double calculationRecursion()
    {
        double x = calculation();
        return x;
    }
    private double calculation()
    {
        if (ch == '+')
        {
            currentCh();
            return calculation();
        }
        if (ch == '-')
        {
            currentCh();
            return -calculation();
        }
        double res = 0;
        if (ch == '(')
        {
            currentCh();
            res = secondPriority();
            if (ch != ')')
                result = "Error in calculation!";
            else
            {
                currentCh();
                if (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z')
                {
                    currentCh();
                    res *= calculation();
                }
            }
        }
        else
            if ((ch >= '0' && ch <= '9') || ch =='.')
            {
                int startInd = this.index;
                while ((ch >= '0' && ch <= '9') || ch =='.')
                    currentCh();
                res = Double.parseDouble(this.strExpression.substring(startInd, this.index));
                if (ch >= 'a' && ch <= 'z' || ch == '(')
                {
                    currentCh();
                    res *= calculation();
                }
            }
            else
                if (ch >= 'a' && ch <= 'z')
                {
                    int startInd = this.index;
                    while (ch >= 'a' && ch <= 'z' && this.index < startInd + 4)
                        currentCh();
                    String function = this.strExpression.substring(startInd, this.index);
                    if (function.equals("sqrt"))
                        res = Math.sqrt(calculation());
                    else
                    {
                        previousCh();
                        function = this.strExpression.substring(startInd, this.index-1);
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
                                                double currentVariable = 0;
                                                if (variables.containsKey(ch))
                                                {
                                                    currentVariable = variables.get(ch);
                                                }
                                                else
                                                {

                                                }


                                             }
                    }
                }
        return res;
    }
}
