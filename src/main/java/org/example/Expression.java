package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Mathematical expressions class
 */
public class Expression {
    /**
     * Mathematical expression in the form of a string
     */
    private String strExpression;
    /**
     * The value of a mathematical expression (or the type of error) in the form of a string
     */
    private String result;
    /**
     * Index of the current character
     */
    private int index;
    /**
     * Current symbol
     */
    private char ch;
    /**
     * Dictionary of Values of variables in the form Map
     */
    private Map variables;
    public Expression (String str)
    {
        this.strExpression = str;
        this.index =-1;
        variables = new HashMap<>();
    }
    public Expression ()
    {
        this.strExpression = "";
        this.index =-1;
        variables = new HashMap<>();
    }
    /**
     * The method shifts the index of the current character to the right by 1 and changes the current character
     */
    private void currentCh()
    {
        this.ch = ++this.index < this.strExpression.length() ? this.strExpression.charAt(this.index) : '@';
        //System.out.println(ch);
    }
    /**
     * The method shifts the index of the current character to the left by 1 and changes the current character
     */
    private void previousCh()
    {
        this.ch = --this.index >= 0 ? this.strExpression.charAt(this.index) : '@';
        //System.out.println(ch);
    }

    /**
     * The method adds (modifies) the value of the variable
     * @param key name of the variable to be added (modified)
     * @param value value of the variable to be added (modified)
     */
    public void setVariable(char key, double value)
    {
        variables.put(key,value);
    }
    /**
     * The method calculates the value of a mathematical expression
     * written in the class after clearing the dictionary of variables
     * @return the calculated expression (8 decimal places) or the type of the first error that occurred during the calculation
     */
    public String calculateExpression ()
    {
        return calculateExpression(null, true);
    }
    /**
     * The method calculates the value of the mathematical expression passed to the function
     * @param st mathematical expression to be calculated
     * @param clearMap is true if you need to clear the dictionary of variables already in the class
     * @return the calculated expression (8 decimal places) or the type of the first error that occurred during the calculation
     */
    public String calculateExpression (String st, boolean clearMap)
    {
        if (st!=null)
            strExpression = st;
        if (clearMap)
            variables.clear();
        if (strExpression != null && !strExpression.equals(""))
        {
            if(checkExpression(null))
            {
                result = "";
                index = -1;
                double meanExpression;
                currentCh();
                meanExpression = secondPriority();
                if (result.equals(""))
                {
                    result = String.format("%.8f", meanExpression);
                    result = result.replaceAll(",", ".");
                    double res = Double.parseDouble(result);
                    result = Double.toString(res);
                }
            }
            else
                result = "Syntax error in the expression!";
        }
        else
            result = "The expression is not set!";
        return this.result;
    }
    /**
     * The method checks (and places in the class) the mathematical expression passed to the function for syntax errors
     * @param str mathematical expression to check
     * @return is true if the mathematical expression being tested does not contain syntax errors
     */
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
    /**
     * The method checks the mathematical expression written in the class for syntax errors
     * @return is true if the mathematical expression being tested does not contain syntax errors
     */
    public boolean checkExpression()
    {
        return checkExpression(null);
    }
    /**
     * The method performs recursive descent on the expression (while maintaining the priority of operations)
     * by calculating the product and quotient of already calculated expressions
     * @return calculated value
     */
    private double firstPriority()
    {
        double x = calculation();
        for (;;)
        {
            if (ch == '*' && result.equals(""))
            {
                currentCh();
                x *= calculation();
            }
            else
                if (ch == '/' && result.equals(""))
                {
                    currentCh();
                    double divider = calculation();
                    if (divider!=0)
                        x /= divider;
                    else if (result.equals(""))
                        result = "Error in calculation (division by zero)!";
                }
                else
                    return x;
        }
    }
    /**
     * The method performs recursive descent on the expression (while maintaining the priority of operations)
     * by calculating the sum and difference of already calculated expressions
     * @return calculated value
     */
    private double secondPriority()
    {
        double x = firstPriority();
        for (;;)
        {
            if (ch == '+' && result.equals(""))
            {
                currentCh();
                x += firstPriority();
            }
            else
                if (ch == '-' && result.equals(""))
                {
                    currentCh();
                    x -= firstPriority();
                }
                else
                    return x;
        }
    }
    /**
     * The method calculates the value of the expression starting from the current index
     * @return calculated value (if it is impossible to calculate, it will write the error type in the result and return 0.0)
     */
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
        Double res = null;
        if (ch == '(')
        {
            currentCh();
            res = secondPriority();
            currentCh();
        }
        else
            if ((ch >= '0' && ch <= '9'))
            {
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
                    if (result.equals(""))
                        result = "Error in calculation (incorrect number format)!";
                    res = 0.0;
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
                    {
                        double sq = calculation();
                        if (sq>=0)
                            res = Math.sqrt(sq);
                        else
                        {
                            if (result.equals(""))
                                result = "Error in calculation (the value in sqrt is less than 0)!";
                            res = 0.0;
                        }
                    }
                    else
                    {
                        if (function.length() == 4)
                            previousCh();
                        function = this.strExpression.substring(startInd, this.index);

                        switch (function)
                        {
                            case "sin":
                                res = Math.sin(calculation());
                                break;
                            case "cos":
                                res = Math.cos(calculation());
                                break;
                            case "tan":
                                res = Math.tan(calculation());
                                break;
                            case "abs":
                                res = Math.abs(calculation());
                                break;
                            default:
                                this.index = startInd;
                                ch = this.strExpression.charAt(this.index);
                                double currentVariable;
                                if (variables.containsKey(ch))
                                {
                                    currentVariable = (double) variables.get(ch);
                                }
                                else
                                {
                                    Scanner in = new Scanner(System.in);
                                    System.out.print("Value " + ch + " -> ");
                                    currentVariable = in.nextDouble();
                                    setVariable(ch, currentVariable);
                                }
                                res = currentVariable;
                                currentCh();
                                break;
                        }
                    }
                }
        if (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'z' || ch == '(')// || ch == '.' || ch == ',')
            res *= calculation();
        if (ch =='^')
        {
            currentCh();
            double degree = calculation();
            if (res<0 && degree <1)
            {
                if (result.equals(""))
                    result = "Error in calculation (it is impossible to raise to a degree)!";
                res = 0.0;
            }
            else
                res = Math.pow(res,degree);
        }
        return res;
    }
    /**
     * The method set new mathematical expression in class
     * @param strExpression new mathematical expression
     */
    public void setStrExpression(String strExpression) {
        this.strExpression = strExpression;
    }
    /**
     * The method set new Dictionary of Values of variables in class
     * @param variables new Dictionary of Values of variables
     */
    public void setVariables(Map variables) {
        this.variables = variables;
    }
    /**
     * The method return Dictionary of Values of variables stored in a class
     * @return Dictionary of Values of variables stored in a class
     */
    public Map getVariables() {
        return variables;
    }
    /**
     * The method return mathematical expression stored in a class
     * @return mathematical expression stored in a class
     */
    public String getStrExpression() {
        return strExpression;
    }
}
