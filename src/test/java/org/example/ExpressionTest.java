package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExpressionTest {
    Expression mathExp = new Expression();
    @BeforeEach
    public void prepareData(){
    mathExp.setVariable('a',5.5);
    mathExp.setVariable('b',3.2);
    mathExp.setVariable('c',-0.5);
    mathExp.setVariable('d',-6.3);
    }

    @Test
    void testCalculateExpressionZero() {
        //5.5*5.5-5.5+(-0.5)/(-6.3)^2
        assertEquals("24.73740237",mathExp.calculateExpression("a*a-a+c/d^2",false));
    }
    @Test
    void testCalculateExpressionOne() {
        //sqrt(-0.5)
        assertEquals("Error in calculation (the value in sqrt is less than 0)!",mathExp.calculateExpression("sqrtc",false));
    }
    @Test
    void testCalculateExpressionTwo() {
        //cos(sqrt(abs(-6.5)-5.5)-1)-9
        assertEquals("-8.0", mathExp.calculateExpression("cos(sqrt(abs-6.5-a)-1)+-9", false));
    }
    @Test
    void testCalculateExpressionThree() {
        //5.5*5*5.5+0.5
        assertEquals("151.75", mathExp.calculateExpression("a5a-c", false));
    }
    @Test
    void testCalculateExpressionFour() {
        //1/(5.5-5.5)
        assertEquals("Error in calculation (division by zero)!", mathExp.calculateExpression("1/(a-5.5)", false));
    }
    @Test
    void testCalculateExpressionFive() {
        //5.5*2^(2^2)
        assertEquals("88.0", mathExp.calculateExpression("a2^2^2", false));
    }
    @Test
    void testCalculateExpressionSix() {
        //2+2-3.4.4
        assertEquals("Syntax error in the expression!", mathExp.calculateExpression("2+2-3.4.4", false));
    }
    @Test
    void testCheckExpressionZero() {
        assertEquals(true,mathExp.checkExpression("a*a-a+c/d^t"));
    }
    @Test
    void testCheckExpressionOne() {
        assertEquals(false,mathExp.checkExpression("a*a-a+/d^t"));
    }
    @Test
    void testCheckExpressionTwo() {
        // a*cos(a*b)
        assertEquals(true,mathExp.checkExpression("acosab"));
    }
    @Test
    void testCheckExpressionThree() {
        // cos(sqrt(abs(-1)))
        assertEquals(true,mathExp.checkExpression("cossqrtabs-1"));
    }
    @Test
    void testCheckExpressionFour() {
        // cos(sqrt(a^(bc)))-1
        assertEquals(true,mathExp.checkExpression("cossqrta^bs-1"));
    }
    @Test
    void testCheckExpressionFive() {
        // cos(sqrt(^(bc)))-1
        assertEquals(false,mathExp.checkExpression("cossqrt^bs-1"));
    }
}