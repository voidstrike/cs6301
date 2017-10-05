package cs6301.lp1;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Alan Lin on 9/15/2017.
 */
public class Num implements Comparable<Num>{
    static long defaultBase = 10;
    long base = defaultBase;
    LinkedList<Long> numList;
    int numLength;
    boolean sign; // true means positive, otherwise negative

    // Constructors
    public Num(){
        // Create a new empty Num class with default base
        numList = new LinkedList<>();
        sign = true;
        numLength = 0;
    }

    public Num(String s){
        numList = new LinkedList<>();
        // Create Num class with String, default base 10
        for(int i = s.length()-1; i > 0; i--){
            numList.add(Long.valueOf(s.charAt(i) - '0'));
        }

        if (s.charAt(0) == '-'){
            sign = false; // detect a negative sign, set this number to negative
        }
        else if (s.charAt(0) == '+'){
            sign = true; // detect a explicit positive sign, set the number to positive
        }
        else{
            // the first character is a number, add that value to the end of numList
            numList.add(Long.valueOf(s.charAt(0) - '0'));
            sign = true;
        }

        numLength = numList.size();
    }

    public Num(long x){
        this(String.valueOf(x));
    }

    static Num add(Num a, Num b){
        // suppose they have same base now
        Num result;
        Iterator aIter = a.numList.iterator(), bIter = b.numList.iterator();
        if (a.sign == b.sign){
            result = supportAdd(aIter, bIter);
            result.sign = a.sign;
        }
        else{
            // different Sign
            if (a.sign){
                // a > 0
                b.sign = true;
                if (a.compareTo(b) >= 0){
                    result = supportSub(aIter, bIter);
                    result.sign = true;
                }
                else{
                    result = supportSub(bIter, aIter);
                    result.sign = false;
                }
                b.sign = false;
            }
            else{
                a.sign = true;
                if (a.compareTo(b) >= 0){
                    result = supportSub(aIter, bIter);
                    result.sign = false;
                }
                else{
                    result = supportSub(bIter, aIter);
                    result.sign = true;
                }
                a.sign = false;
            }
        }

        return result;
    }

    static Num subtract(Num a, Num b){
        Num result;
        Iterator aIter = a.numList.iterator(), bIter = b.numList.iterator();
        // TODO
        if (a.sign != b.sign){
            result = supportAdd(aIter, bIter);
            result.sign = a.sign;
        }
        else{
            // Same sign
            if(a.sign){
                // a and b larger than 0
                if (a.compareTo(b) >= 0){
                    result = supportSub(aIter, bIter);
                    result.sign = true;
                }
                else{
                    result = supportSub(bIter, aIter);
                    result.sign = false;
                }
            }
            else{
                // both a and b less than 0
                if (a.compareTo(b) < 0){
                    result = supportSub(aIter, bIter);
                    result.sign = false;
                }
                else{
                    result = supportSub(bIter, aIter);
                    result.sign = true;
                }
            }
        }
        return result;
    }

    static Num product(Num a, Num b){
        // Karatsuba Algorithm
        // Brute-force Algorithm
        Num result = new Num(0L);
        Num tmp;
        long holder;
        Iterator bIter = b.numList.iterator();
        int i = 0;
        while (bIter.hasNext()){
            holder = (long) bIter.next();
            tmp = time(a, holder);
            tmp.shiftKbit(i);
            i+=1;
            result = add(result, tmp);
        }
        return result;
    }

    static Num time(Num a, long p){
        Num result = new Num();
        long carry = 0, tmp;
        Iterator aIter = a.numList.iterator();
        while (aIter.hasNext()){
            tmp = (long) aIter.next();
            result.numList.add((carry + tmp * p) % result.base);
            carry = (carry + tmp * p) / result.base;
        }
        while (carry > 0){
            result.numList.add(carry % result.base);
            carry /= result.base;
        }
        return result;
    }

    static Num power(Num a, long n){
        Num result = new Num();
        if (n == 0){
            result.numList.add( (long) 0 );
        }
        else if (n == 1){
            result = a;
        }
        else{
            result = power(a, n>>>1);
            result = product(result, result);
            if (n % 2 != 0)
                result = product(a, result);
        }
        return result;
    }

    void printList() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.base + " :");
        Iterator iter = this.numList.iterator();
        while(iter.hasNext())
            sb.append(" " + iter.next());
        System.out.println(sb.toString());
    }

    // Auxiliary Functions
    public int compareTo(Num other){
        Long thisBlock, otherBlock;

        if (this.sign && !other.sign) // this Num is positive and other is negative
            return 1;
        else if (other.sign && !this.sign) // this Num is negative and other is positive
            return -1;
        else{
            // Same sign
            if (this.numLength > other.numLength)
                return this.sign ? 1 : -1;
            else if (this.numLength == other.numLength){
                //TODO
                Iterator thisIter = this.numList.descendingIterator();
                Iterator otherIter = other.numList.descendingIterator();
                while(thisIter.hasNext()){
                    // Since numLength is equal, only need to check one iterator.hasNext()
                    thisBlock = (Long) thisIter.next();
                    otherBlock = (Long) otherIter.next();
                    if (thisBlock.compareTo(otherBlock) == 1)
                        return this.sign ? 1 : -1;
                    else if (thisBlock.compareTo(otherBlock) == -1)
                        return this.sign ? -1 : 1;
                }
                return 0; // all the blocks is equal, return 0;
            }
            else{
                return this.sign ? -1 : 1;
            }
        }
    }

    private static Num supportAdd(Iterator<Long> aIter, Iterator<Long> bIter){
        // in this method, ignore the sign of each Num
        Num result = new Num();
        Long aBlock, bBlock;
        long carry = 0;
        while(aIter.hasNext() && bIter.hasNext()){
            aBlock = aIter.next();
            bBlock = bIter.next();
            result.numList.add((aBlock+bBlock+carry) % result.base);
            carry = (aBlock+bBlock+carry) / result.base;
        }
        assert !aIter.hasNext() || !bIter.hasNext();
        while(aIter.hasNext()){
            aBlock = aIter.next();
            result.numList.add((aBlock+carry) % result.base);
            carry = (aBlock+carry) / result.base;
        }
        while(bIter.hasNext()){
            bBlock = bIter.next();
            result.numList.add((bBlock+carry) % result.base);
            carry = (bBlock+carry) / result.base;
        }
        if (carry > 0){
            result.numList.add(carry);
        }
        result.numLength = result.numList.size();
        return result;
    }

    private static Num supportSub(Iterator<Long> a, Iterator<Long> b){
        // In this method, a will always have large or equal abs value than b
        Num result = new Num();
        Long aBlock, bBlock;
        long carry = 0;
        while (b.hasNext()){
            aBlock = a.next();
            bBlock = b.next();
            if (aBlock - bBlock - carry >= 0){
                result.numList.add(aBlock - bBlock - carry);
                carry = 0;
            }
            else{
                result.numList.add(aBlock - bBlock - carry + result.base);
                carry = 1;
            }
        }
        while(a.hasNext()){
            aBlock = a.next();
            if (aBlock - carry >= 0){
                result.numList.add(aBlock - carry);
                carry = 0;
            }
            else{
                result.numList.add(aBlock - carry + result.base);
                carry = 1;
            }
        }

        // Remove additional 0 in front of numList
        while (result.numList.size() > 1 && result.numList.peekLast() == 0){
            result.numList.removeLast();
        }

        result.numLength = result.numList.size();
        return result;
    }

    private void shiftKbit(int k){
        for (int i=0; i<k; i++){
            this.numList.addFirst(0L);
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        Iterator thisIter = this.numList.descendingIterator();
        if (!this.sign)
            sb.append("-");
        while (thisIter.hasNext()){
            sb.append(thisIter.next());
        }
        return sb.toString();
    }

    public static void main(String[] args){
        // Test code below
        Num x = new Num("98765432123456789012456789012646378589165127456376");
        Num y = new Num("56698364876147630847612984618476284587653095761286");
        Num z = new Num(98765432123456789L);
        Num e = new Num("85849037612648764376549098612765874365348765673543");
        Num e2 = new Num("-85849037612648764376549098612765874365348765673543");
        Num f = new Num("566983648761476308476145");
        Num c = subtract(x, y);
        Num g = add(f, e);
        //Num d = Num.product(c, z);
        Num d = power(z, 8);
        System.out.println(c);
        System.out.println(d);
        d.printList();
    }
}
