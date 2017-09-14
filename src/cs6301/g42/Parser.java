package cs6301.g42;

import java.util.ArrayDeque;
import java.util.HashMap;

/**
 * Created by Alan Lin on 9/6/2017.
 */
// SP5 Q7
public class Parser {

    public static HashMap<Character, Integer> createOperatorTable(){
        HashMap<Character, Integer> operatorPriorityTable = new HashMap<>();
        operatorPriorityTable.put('+', 1);
        operatorPriorityTable.put('-', 1);
        operatorPriorityTable.put('*', 2);
        operatorPriorityTable.put('/', 2);
        operatorPriorityTable.put('^', 3);
        operatorPriorityTable.put('!', 4);
        return operatorPriorityTable;
    }

    public static String getNumeric(String operatorStr, int index){
        String result= "";
        Character tmp;
        int maxSize = operatorStr.length();
        for(;index<maxSize;index++){
            tmp = operatorStr.charAt(index);
            if (tmp >= '0' && tmp <= '9'){
                result += String.valueOf(tmp);
            }
            else break;
        }
        index--;
        return result;
    }

    public static String arthmeticParse(String expression){
        ArrayDeque<Character> operatorStack = new ArrayDeque<>();
        HashMap<Character, Integer> priorityTable = createOperatorTable();
        String outputString = "";

        String currentNumber;
        Character tmp, examOperator; // Used in several phase to store temporary Char
        Character currentChar;

        for (int i = 0; i<expression.length(); i++){
            currentChar = expression.charAt(i);
            if (currentChar>='0' && currentChar<='9'){
                // We the token we read is a number
                currentNumber = getNumeric(expression, i);
                i += currentNumber.length() - 1; // update i
                outputString += currentNumber + " ";
            }
            else if (currentChar == '('){
                // left bracket, push onto stack
                operatorStack.push(currentChar);
            }
            else if (currentChar == ')'){
                // right bracket, pop until first left bracket is encountered
                if (operatorStack.isEmpty())
                    return "Error"; // Expression has some error, parsing failed
                else{
                    tmp = operatorStack.pop();
                    while (tmp != '('){
                        outputString += String.valueOf(tmp) + " ";
                        if (operatorStack.isEmpty())
                            return "Error"; // Expression has some error, parsing failed
                        tmp = operatorStack.pop();
                    }
                }
            }
            else{
                // token is a operator
                tmp = operatorStack.peekFirst(); // try get top element in the stack
                while(tmp != null){
                    if (tmp == '(') break;
                    else if (priorityTable.get(currentChar) <= priorityTable.get(tmp) && tmp != '^'){
                        examOperator = operatorStack.pop();
                        outputString += String.valueOf(examOperator) + " ";
                        tmp = operatorStack.peekFirst(); // Update tmp
                    }
                    else{
                        break;
                    }
                }
                operatorStack.push(currentChar);
            }

        }

        while(!operatorStack.isEmpty()){
            outputString += String.valueOf(operatorStack.pop())+" ";
        }

        return outputString;
    }


    public static void main(String[] args){
        String test = arthmeticParse("3+4*2/(123-5)^2^3");
        System.out.print(test);
    }
}
