package cs6301.lp1;

import java.util.Scanner;
import java.util.HashMap;
import java.util.ArrayDeque;

/**
 * Created by Alan Lin on 10/5/2017.
 * @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */
public class L3Driver {

    private void readInput(Scanner in){
        HashMap<String, Num> vars = new HashMap<>();
        ArrayDeque<Num> cStack = new ArrayDeque<>();
        String varName;
        Num sNum, tmpA, tmpB, last = null;

        while(in.hasNext()){
            String word = in.next();
            if (word.equals(";"))
                break;
            else{
                varName = word;
                in.next(); // get rid of equal sign
                while(in.hasNext()){
                    word = in.next();
                    if(word.equals(";")){
                        sNum = cStack.pop();
                        System.out.println(sNum.toString());
                        last = sNum;
                        vars.put(varName, sNum);
                        break;
                    }
                    else{
                        if (vars.containsKey(word)){
                            cStack.push(vars.get(word));
                        }
                        else{
                            switch (word){
                                case("+"):{
                                    tmpA = cStack.pop();
                                    tmpB = cStack.pop();
                                    cStack.push(Num.add(tmpA, tmpB));
                                    break;
                                }
                                case("-"):{
                                    tmpA = cStack.pop();
                                    tmpB = cStack.pop();
                                    cStack.push(Num.subtract(tmpB, tmpA));
                                    break;
                                }
                                case("*"):{
                                    tmpA = cStack.pop();
                                    tmpB = cStack.pop();
                                    cStack.push(Num.product(tmpA, tmpB));
                                    break;
                                }
                                case("/"):{
                                    tmpA= cStack.pop();
                                    tmpB= cStack.pop();
                                    cStack.push(Num.divide(tmpB, tmpA));
                                    break;
                                }
                                case("^"):{
                                    tmpA= cStack.pop();
                                    tmpB= cStack.pop();
                                    cStack.push(Num.power(tmpB, tmpA));
                                    break;
                                }
                                case ("%"):{
                                    tmpA= cStack.pop();
                                    tmpB= cStack.pop();
                                    cStack.push(Num.mod(tmpB, tmpA));
                                    break;
                                }
                                case("|"):{
                                    tmpA = cStack.pop();
                                    cStack.push(Num.squareRoot(tmpA));
                                    break;
                                }
                                default:{
                                    cStack.push(new Num(word));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (last != null)
            last.printList();
    }

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        L3Driver x = new L3Driver();
        x.readInput(in);
    }
}
