package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        Scanner in = new Scanner(System.in);
        String str = in.nextLine();
        //System.out.println(str);
        Expression exp = new Expression(str);
        exp.setVariable('a',5);
        exp.setVariable('b',3.3);
        String res = exp.calculateExpression(str,false);
        //System.out.println("Hello world!");

        System.out.println(res);

        //Double a = in.nextDouble();
        //Double b = in.nextDouble();
        //System.out.println(a);
        //System.out.println(b);
    }
}