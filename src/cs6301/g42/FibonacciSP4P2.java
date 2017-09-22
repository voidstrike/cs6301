//** @author Khaled Al-naami, Peter Farago, Yu Lin, David Tan

package cs6301.g42;

import java.math.*;
import cs6301.g00.*;

public class FibonacciSP4P2 {
	// Do a simple linear scan algorithm: Fib[n] = Fin[n-1] + Fin[n-2];
	// Since numbers are stored with BigInteger class, use add for "+"
	public static BigInteger linearFibonacci(int n) {
		BigInteger previous = BigInteger.ZERO;
		BigInteger current = BigInteger.ONE;
		BigInteger next;
		
		if( n == 0){
			return BigInteger.ZERO;
		}else{
			for(int i = 1; i < n; i++){
				next = previous.add(current);
				previous = current;
				current = next;
				
			}
			return current;
		}
		
		
	}
    
	// Implement O(log n) algorithm described in class (Sep 15)
	public static BigInteger logFibonacci(int n) {
		BigInteger F[][] = new BigInteger[][]{{BigInteger.ONE,BigInteger.ONE}, {BigInteger.ONE,BigInteger.ZERO}};
		
		if(n == 0){
			return BigInteger.ZERO;
		}
		power(F, n-1);
		return F[0][0];
	}
	
	// The power function solve the problem recursively to make it run in log( n) time. 
	// use a Tmp matrix to represent the original matrix F[][], 
	// and multiply the result with Tmp if n is an odd number.
	private static void power(BigInteger F[][], int n){
		if( n==0 || n==1){
			return;
		}
		BigInteger Tmp[][] = new BigInteger[][]{{BigInteger.ONE,BigInteger.ONE}, {BigInteger.ONE,BigInteger.ZERO}};
		
		power(F, n/2);
		multiplication(F, F);
		
		if( n%2 == 1){
			multiplication(F, Tmp);
		}
	}
	
	// it is a 2by2 * 2by2 matrix multiplication function
	private static void multiplication(BigInteger F[][], BigInteger Tmp[][]){
		BigInteger f1 =  F[0][0].multiply(Tmp[0][0]).add(F[0][1].multiply(Tmp[1][0]));
	    BigInteger f2 =  F[0][0].multiply(Tmp[0][1]).add(F[0][1].multiply(Tmp[1][1]));
	    BigInteger f3 =  F[1][0].multiply(Tmp[0][0]).add(F[1][1].multiply(Tmp[1][0]));
	    BigInteger f4 =  F[1][0].multiply(Tmp[0][1]).add(F[1][1].multiply(Tmp[1][1]));
	    
	    F[0][0] = f1;
	    F[0][1] = f2;
	    F[1][0] = f3;
	    F[1][1] = f4;
	}
	
	public static void main(String args[]){
		Integer n = 1000000;
		Timer t = new Timer();
		t.start();
		//System.out.println(linearFibonacci(n));
		linearFibonacci(n);
		System.out.println(t.end());
		t.start();
		//System.out.println(logFibonacci(n));
		logFibonacci(n);
		System.out.println(t.end());
	}
}
/*
n = 1000000
sample output:

Time: 34653 msec.
Memory: 46 MB / 162 MB.
Time: 475 msec.
Memory: 43 MB / 155 MB.


Time: 29221 msec.
Memory: 72 MB / 185 MB.
Time: 455 msec.
Memory: 71 MB / 200 MB.


Time: 27646 msec.
Memory: 86 MB / 175 MB.
Time: 428 msec.
Memory: 44 MB / 166 MB.

*/