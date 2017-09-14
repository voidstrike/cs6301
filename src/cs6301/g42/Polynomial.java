package cs6301.g42;
import java.util.Scanner;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collections;

public class Polynomial
{
	private LinkedList<Term> terms;

	public Polynomial(String input)
	{
		terms = new LinkedList<Term>();
		parseInput(input);
	}

	public Polynomial()
	{
		terms = new LinkedList<Term>();
	}

	private void parseInput(String input)
	{
		String[] tokens = input.split(" ");
		int currentCoefficient, currentExponent;

		for(int i = 0; i < tokens.length-1; i+=2)				// parse input string and add Terms to linked list one by one
		{
			currentCoefficient = Integer.parseInt(tokens[i]);
			currentExponent = Integer.parseInt(tokens[i+1]);
			terms.add(new Term(currentCoefficient, currentExponent));
		}

		this.sortByExponent();
	}

	public void addTerm(Term term)
	{
		this.terms.add(term);
	}

	public double evaluate(double x)
	{
		double result = 0.0;
		Iterator<Term> iterator = this.terms.iterator();
		Term currentTerm = next(iterator);
		int currentCoefficient, currentExponent;

		while(currentTerm != null)			// iterate through each term of polynomial, adding the value of each term to the result
		{
			currentCoefficient = currentTerm.getCoefficient();
			currentExponent = currentTerm.getExponent();

			result += currentCoefficient*Math.pow(x, (double)currentExponent);
			currentTerm = next(iterator);
		}

		return result;

	}

	public Polynomial multiply(Polynomial other)
	{

		Polynomial result = new Polynomial();
		Iterator<Term> iterator1 = this.terms.iterator();
		Iterator<Term> iterator2 = other.terms.iterator();

		Term pol1Term = next(iterator1);						// get and store initial terms from both polynomials 
		Term pol2Term = next(iterator2);
		Term productTerm;

		while(pol1Term != null)				// multiply each term of this polynomial by each term of other polynomial and add each product to result 
		{

			while(pol2Term != null)
			{
				productTerm = pol1Term.multiply(pol2Term);
				result.addTerm(productTerm);
				pol2Term = next(iterator2);
			}

			pol1Term = next(iterator1);
			iterator2 = other.terms.iterator();
			pol2Term = next(iterator2);
		}

		result.sortByExponent();			// sort polynomial by exponent and simplify
		simplify(result);
		
		return result;					

	}

	public void simplify(Polynomial pol)
	{
		// simplify input polynomial by combining like terms
		// input polynomial is assumed to be sorted by increasing exponent

		LinkedList<Term> polTerms = pol.terms;
		int polTermsLength = polTerms.size();
		Term currentTerm, nextTerm;
		int currentCoefficient, currentExponent, currentSum, currentIndex = 0, nextExponent;

		while(currentIndex != polTermsLength - 1)				
		{
			currentTerm = polTerms.get(currentIndex);
			currentCoefficient = currentTerm.getCoefficient();
			currentExponent = currentTerm.getExponent();
			currentSum = currentCoefficient;
			nextTerm = polTerms.get(currentIndex+1);
			nextExponent = nextTerm.getExponent();

			while(nextExponent == currentExponent)
			{
				currentSum += nextTerm.getCoefficient();
				polTerms.remove(currentIndex+1);
				nextTerm = polTerms.get(currentIndex+1);
				nextExponent = nextTerm.getExponent();
			}

			currentTerm.setCoefficient(currentSum);
			polTermsLength = polTerms.size();
			currentIndex++;	
		}


	}

	public void sortByExponent()
	{
		Collections.sort(this.terms);
	}

	public Polynomial add(Polynomial other)
	{
		Iterator<Term> iterator1 = this.terms.iterator();
		Iterator<Term> iterator2 = other.terms.iterator();

		Term pol1Term = next(iterator1);						// get and store initial terms from both polynomials 
		Term pol2Term = next(iterator2);
		int compareToValue;
		int newCoefficient;										// newCoefficient will store summed up coefficients
		Polynomial result = new Polynomial();

		while(pol1Term != null && pol2Term != null) 		// run loop until at least one of the polynomials has been fully iterated
		{
			compareToValue = pol1Term.compareTo(pol2Term);

			if(compareToValue == -1)		 			// if one term's exponent is smaller, add that term to result and move to next term in the corresponding polynomial
	   	  	{
	   	  		result.addTerm(pol1Term);
	   	  		pol1Term = next(iterator1);
	   	  	}
	   	  	else if(compareToValue == 1)	
	   	  		 {	
		   	  		result.addTerm(pol2Term);
	   	  			pol2Term = next(iterator2);
	   	  		 }
	   	  		 else									// if exponents are equal, add new term with summed up coefficients to result and move to next item in both polynomials
	   	  		 {
	   	  		 	newCoefficient = pol1Term.getCoefficient() + pol2Term.getCoefficient();	  									
	   	  		 	result.addTerm(new Term(newCoefficient, pol1Term.getExponent()));				
		   	  		pol1Term = next(iterator1);
		   	  		pol2Term = next(iterator2);
	   	  		 }

		}

		if(pol1Term == null && pol2Term == null)			// if both polynomials have been completely iterated, quit
		{
			return result;
		}
		else											// otherwise add remaining terms from either polynomial
		{
			if(pol1Term != null)
			{
				while(pol1Term != null)
				{
					result.addTerm(pol1Term);
	   	  			pol1Term = next(iterator1);
				}
			}
			else
			{
				while(pol2Term != null)
				{
					result.addTerm(pol2Term);
	   	  			pol2Term = next(iterator2);
				}
			}
		}

		return result; 
	}

	// helper method helps write cleaner code with iterator

	public static Term next(Iterator<Term> iterator)        
	{
		if(iterator.hasNext())
		{
			return iterator.next();
		}
		else
		{
			return null;
		}
	}

	public String toString()
	{
		// method returns string representation of polynomial in the form a0 + a1*x + a2*x^2 + ... + an*x^n. 

		String outputString = "";
		Iterator<Term> iterator = terms.iterator();
		Term currentTerm;
		int currentCoefficient, currentExponent;

		while(iterator.hasNext())			// loop iterates through each Term one by one and adds corresponding string to output
		{
			currentTerm = iterator.next();
			currentCoefficient = currentTerm.getCoefficient();
			currentExponent = currentTerm.getExponent();

			if(currentExponent == 0)
			{
				outputString += currentCoefficient + " ";
			}
			else
			{
				if(currentCoefficient < 0)
				{
					if(currentCoefficient == -1)
					{
						outputString += "- ";
					}
					else
					{
				 	    outputString += "- " + -1*currentCoefficient;
				 	}
				}
				else
				{
					if(currentCoefficient == 1)
					{
						outputString += "+ ";
					}
					else
					{
						outputString += "+ " + currentCoefficient;
					}
				 	
				}

				if(currentExponent == 1)
				{
				    outputString += "x ";
				}
				else
				{
				    outputString += "x^" + currentExponent + " ";  
				}
			
			}


		}

		if(outputString.split(" ")[0].equals("+"))			// if first character of output string is '+' then remove it
		{
			outputString = outputString.replaceFirst("\\+", "").trim();
		}

		return outputString;

	}

	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		String inputString = "";

		System.out.println("Please enter first polynomial: ");
		inputString = scanner.nextLine();
		Polynomial p1 = new Polynomial(inputString);

		System.out.println("Please enter second polynomial: ");
		inputString = scanner.nextLine();
		Polynomial p2 = new Polynomial(inputString);

		Polynomial p3 = p1.multiply(p2);

		System.out.println(p1);
		System.out.println(p2);
		System.out.println(p3);
	}

}