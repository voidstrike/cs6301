package cs6301.lp1;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Alan Lin on 9/15/2017.
 * @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */
public class Num implements Comparable<Num>{

    private static long defaultBase = 10;
    private long base = defaultBase;
    private LinkedList<Long> numList;
    private int numLength;
    private boolean sign; // True means positive, otherwise negative

    // Static numbers
    private static final Num pivot0 = new Num(0L);
    private static final Num pivot1 = new Num(1L);
    private static final Num pivot2 = new Num(2L);

    // Constructors
    public Num(){
        // Create a new empty Num class with default base
        // No initial value
        numList = new LinkedList<>();
        sign = true;
        numLength = 0;
    }

    public Num(String s){
        numList = new LinkedList<>();
        // Create Num class with String, default base 10
        for(int i = s.length()-1; i > 0; i--){
            numList.add((long) s.charAt(i) - '0');
        }

        if (s.charAt(0) == '-'){
            sign = false; // Detected a negative sign, set negative
        }
        else if (s.charAt(0) == '+'){
            sign = true; // Detected an explicit positive sign, set positive
        }
        else{
            // The first character is a digit, add the value of that digit into the numList
            numList.add((long) s.charAt(0) - '0');
            sign = true;
        }

        numLength = numList.size();
    }

    public Num(long x){
        this(String.valueOf(x));
    }

    public Num(Num x){
        // return a deep copy of x
        this();
        this.numList.addAll(x.numList);
        this.setLength();
        this.sign = x.sign;
    }

    static Num add(Num a, Num b){
        // suppose they have same base now
        Num result;
        Iterator<Long> aIter = a.numList.iterator(), bIter = b.numList.iterator();
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
        // Finalize the result Num, set numLength and sign
        result.setLength();
        if (result.isZero())
            result.sign = true;
        return result;
    }

    static Num subtract(Num a, Num b){
        Num result;
        Iterator<Long> aIter = a.numList.iterator(), bIter = b.numList.iterator();

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
        // Finalize the result Num, set numLength and sign
        result.setLength();
        if (result.isZero())
            result.sign = true;
        return result;
    }

    static Num product(Num a, Num b){
        // Brute-force Algorithm
        Num result = new Num(0L);
        if (a.isZero() || b.isZero())
            return result;
        Num tmp;
        long holder;
        Iterator bIter = b.numList.iterator();
        int i = 0;
        while (bIter.hasNext()){
            holder = (long) bIter.next();
            tmp = time(a, holder);
            tmp.shiftKbit(i, true);
            i+=1;
            result = add(result, tmp);
        }
        return result;
    }

    private static Num time(Num a, long p){
        Num result = new Num();
        long carry = 0;
        for (Long tmp : a.numList){
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
            result.numList.add(1L);
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
        if (!a.sign && n%2 != 0){
            result.sign = false;
        }
        result.setLength();
        return result;
    }

    static Num power(Num a, Num b){
        Num result = new Num();
        Num tmp;
        if (b.compareTo(pivot0) == 0){
            result.numList.add(1L);
        }
        else if (b.compareTo(pivot1) == 0){
            result = a;
        }
        else{
            tmp = divide(b, pivot2);
            result = power(a, tmp);
            result = power(result, 2);
            tmp = mod(b, pivot2);
            if (pivot0.compareTo(tmp) != 0)
                result = product(a, result);
        }
        if (!a.sign && pivot0.compareTo(mod(b, pivot2)) != 0){
            result.sign = false;
        }
        result.setLength();
        return result;
    }

    static Num divide(Num a, Num b){
        Num result;
        boolean tmpSign = a.sign;
        a.sign = b.sign;
        if(b.isZero())
            return null;

        if (a.compareTo(b) < 0){
            result = new Num(0L);
            return result;
        }
        else if (a.compareTo(b) == 0)
        {
            result = new Num(1L);
            return result;
        }

        Num aux = new Num(b);
        Num auxM = new Num(a);
        Num auxS = new Num(1L);
        result = new Num(0L);
        boolean subSign;

        while(auxM.compareTo(b) >= 0){
            subSign = false;
            while (auxM.compareTo(aux) >= 0){
                subSign = true;
                aux.shiftKbit(1, true);
                auxS.shiftKbit(1, true);
            }
            aux.shiftKbit(1, false);
            auxS.shiftKbit(1, false);
            if (subSign) {
                result = add(result, auxS);
                auxM = subtract(auxM, aux);
            }
        }
        a.sign = tmpSign;
        if (a.sign != b.sign)
            result.sign = false;
        result.setLength();
        return result;
    }

    static Num mod(Num a, Num b){
        Num result;
        boolean tmpSign = a.sign;
        a.sign = b.sign;
        if(b.isZero())
            return null;

        if (a.compareTo(b) < 0){
            result = new Num(a);
            return result;
        }
        else if (a.compareTo(b) == 0)
        {
            result = new Num(0L);
            return result;
        }

        Num aux = new Num(b);
        Num auxM = new Num(a);
        Num auxS = new Num(1L);
        boolean subSign;

        while(auxM.compareTo(b) >= 0){
            subSign = false;
            while (auxM.compareTo(aux) >= 0){
                subSign = true;
                aux.shiftKbit(1, true);
                auxS.shiftKbit(1, true);
            }
            aux.shiftKbit(1, false);
            auxS.shiftKbit(1, false);
            if (subSign) {
                auxM = subtract(auxM, aux);
            }
        }
        result = auxM;
        a.sign = tmpSign;
        if (a.sign != b.sign)
            result.sign = false;
        result.setLength();
        return result;
    }

    static Num squareRoot(Num a){
        if (!a.sign)
            return null;
        Num left = new Num(1L);
        Num right = new Num(a);
        Num res, tmp, oneM;
        while (true){
            res = divide(add(left, right), pivot2);
            tmp = power(res, 2);
            if (tmp.compareTo(a) > 0)
                right = res;
            else if (tmp.compareTo(a) == 0){
                return res;
            }
            else{
                // res less than target
                oneM = add(res, pivot1);
                if (power(oneM, 2).compareTo(a) > 0 ){
                    return res;
                }
                else{
                    left = res;
                }
            }
        }
    }

    void printList() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.base);
        sb.append(" : ");
        for (Long ele : this.numList) {
            sb.append(" ");
            sb.append(ele);
        }
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

    private void shiftKbit(int k, boolean lor){
        // lor = true means add 0 to the left, in the other word, increase the value of this number
        // lor = false decrease the value of this number and ignore the decimal part
        if (lor){
            for (int i=0; i<k; i++){
                this.numList.addFirst(0L);
            }
            this.numLength += k;
        }
        else{
            if (numLength > k){
                for (int i=0; i<k; i++){
                    this.numList.removeFirst();
                }
                numLength -= k;
            }
            else{
                this.numList.clear();
                this.numList.add(0L);
                this.numLength = 1;
            }
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

    private boolean isZero(){
        return numList.size() == 1 && numList.get(0) == 0;
    }

    private void setLength(){
        this.numLength = numList.size();
    }

    public static void main(String[] args){
        // Test code below
//        Num x = new Num("24");
//        Num x2 = new Num("245");
//        Num y = new Num("56698364876147630847612984618476284587653095761286");
//        Num z = new Num(98765432123456789L);
//        Num e = new Num("85849037612648764376549098612765874365348765673543");
//        Num e2 = new Num("-85849037612648764376549098612765874365348765673543");
//        Num f = new Num("566983648761476308476145");
//        //Num c = subtract(x2, x);
//        Num c = divide(e, y);
//        Num g = add(f, e);
//        //Num d = Num.product(c, z);
//        Num d = mod(e, y);
//
//        Num e3 = squareRoot(y);
//        Num test = power(e3, 2);
//        System.out.println(c);
//        System.out.println(d);
//        System.out.println(e3);
//        System.out.println(test);
    }
}
