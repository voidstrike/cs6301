package cs6301.personal;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import cs6301.personal.PolyNode;


/**
 * Created by Alan Lin on 9/6/2017.
 */
public class PolyArithmetic{
    PolyNode head, tail;
    int size;

    public PolyArithmetic(){
        head = new PolyNode(0, 0, null);
        tail = head;
        size = 0;
    }

    // Add one element to this expression class
    public void add(int coef, int power){
        tail.next = new PolyNode(coef, power, null);
        tail = tail.next;
        size++;
    }

    // Add new term into this expression, in order
    public void addOrder(int coef, int power){

        PolyNode compareTerm, prevTerm;
        if (size == 0){
            // If the list is empty, just add this node
            add(coef, power);
            return;
        }
        prevTerm = head;
        compareTerm = head.next;
        while(compareTerm != null){
            if (compareTerm.power == power){
                compareTerm.setCoefficient(compareTerm.coefficient + coef);
                break;
            }
            else if (compareTerm.power > power){
                prevTerm.next = new PolyNode(coef, power, compareTerm);
                size++;
                break;
            }
            else{
                compareTerm = compareTerm.next;
                prevTerm = prevTerm.next;
            }
        }
        // In this case, the power of this new term larger than all term in current expression
        if (compareTerm == null){
           add(coef, power);
        }
    }

    public int getTerm(String expression, int currentIndex){
        // Initizlize all default value
        String result = "";
        String coef = "", power = "";
        Character tmp;
        int maxSize = expression.length();
        boolean sign = true; // denote the sign of the number

        // Reading Phase
        // Step 1. Try to get sign, if there is one
        tmp = expression.charAt(currentIndex);
        if (tmp >= '0' && tmp <= '9' || tmp == 'x') ; // no explicit sign, do nothing
        else if (tmp == '+'){
            result += String.valueOf(tmp);
            tmp = expression.charAt(++currentIndex);
        }
        else if (tmp == '-'){
            sign = false;
            result += String.valueOf(tmp);
            tmp = expression.charAt(++currentIndex);
        }
        else return -1; // Parsing failed, Error Expression

        // Step 2. Try to get coefficient part
        for(; currentIndex < maxSize; currentIndex++){
            tmp = expression.charAt(currentIndex);
            if(tmp >= '0' && tmp <= '9'){
                result += String.valueOf(tmp);
                coef += String.valueOf(tmp);
            }
            else break;
        }

        // Two cases : 1. tmp is a x; 2. tmp is an operator
        // Special case : end of the expression
        if (currentIndex == maxSize - 1){
            // end of the expression and it's a term without power part or a term with power 1
            if (tmp == 'x'){
                // Expression ended with x, the power of this term is 1
                result += String.valueOf(tmp);
                if (sign)
                    addOrder(coef.length() > 0 ? Integer.valueOf(coef) : 1, 1);
                else
                    addOrder(coef.length() > 0 ? -Integer.valueOf(coef) : -1, 1);
            }
            else{
                // Expression ended with number, the power of this term is 0
                if (sign)
                    addOrder(coef.length() > 0 ? Integer.valueOf(coef) : 1, 0);
                else
                    addOrder(coef.length() > 0 ? -Integer.valueOf(coef) : -1, 0);
            }
            return result.length();
        }
        else{
            // it's not the end of this expression, and tmp has two cases
            if (tmp == 'x'){
                // tmp is x, in this case, we may have power, or not
                result += String.valueOf(tmp);
                tmp = expression.charAt(++currentIndex); // read char next to x
                // Same situation, two cases, 1. ^, 2. operator
                if (tmp == '^'){
                    result += String.valueOf(tmp);
                    ++currentIndex;
                    // Start reading power part
                    for (; currentIndex < maxSize; currentIndex++){
                        tmp = expression.charAt(currentIndex);
                        if (tmp >= '0' && tmp <= '9'){
                            result += String.valueOf(tmp);
                            power += String.valueOf(tmp);
                        }
                        else break;
                    }
                    // Read Finished
                    if (sign)
                        addOrder(coef.length() > 0 ? Integer.valueOf(coef) : 1, Integer.valueOf(power));
                    else
                        addOrder(coef.length() > 0 ? -Integer.valueOf(coef) : -1, Integer.valueOf(power));
                }
                else{
                    // read a operator, no power part
                    if (sign)
                        addOrder(coef.length() > 0 ? Integer.valueOf(coef) : 1, 1);
                    else
                        addOrder(coef.length() > 0 ? -Integer.valueOf(coef) : -1, 1);
                }
            }
            else{
                // In this situation, tmp is an operator, which means this term has no power part
                if (sign)
                    addOrder(coef.length() > 0 ? Integer.valueOf(coef) : 1, 0);
                else
                    addOrder(coef.length() > 0 ? -Integer.valueOf(coef) : -1, 0);
            }
        }
        return result.length();
    }

    public double evaluateExpression(double value){
        double result = 0.0;
        PolyNode starter;
        if  (size > 0){
            starter = head.next;
        }
        else
            return -1; // Error Situation

        while(starter != null){
            result += starter.coefficient *  Math.pow((double) value, (double) starter.power);
            starter = starter.next;
        }
        return result;
    }

    public void addExpression(PolyArithmetic otherExpression){
        PolyNode starter;
        if (otherExpression.size == 0){
            return ; // wrong expression
        }
        else{
            starter = otherExpression.head.next;
            while(starter.next != null){
                this.addOrder(starter.coefficient, starter.power);
                starter = starter.next;
            }
        }
    }

    public PolyArithmetic multiExpression(PolyArithmetic otherExpression){
        PolyArithmetic result = new PolyArithmetic();
        if (this.size == 0 || otherExpression.size == 0){
            return result; // one of the expression is empty
        }
        PolyNode thisPointer = head.next, taskPointer = otherExpression.head.next;
        while(taskPointer != null){
            while(thisPointer != null){
                result.addOrder(taskPointer.coefficient * thisPointer.coefficient, taskPointer.power + thisPointer.power);
                thisPointer = thisPointer.next;
            }
            taskPointer = taskPointer.next;
            thisPointer = head.next;
        }
        return result;
    }

    public void readExpression(String expression){
        //TODO
        int termLength;
        if (expression.length() == 0) return; // Wrong input, empty expression
        for (int i=0; i<expression.length(); i++){
            termLength = getTerm(expression, i);
            if(termLength == -1){
                System.out.print("Error");
            }
            else i += termLength - 1;
        }
    }

    public static void main(String[] args){
        PolyArithmetic testExpression = new PolyArithmetic();
        PolyArithmetic testExpression2 = new PolyArithmetic();
        testExpression.readExpression("3+2x^2+x");
        testExpression2.readExpression("3+2x^2+x");
        PolyArithmetic testExpression3 = testExpression.multiExpression(testExpression2);
        System.out.println(testExpression.evaluateExpression(3));
        System.out.println(testExpression3.evaluateExpression(3));

    }

}
