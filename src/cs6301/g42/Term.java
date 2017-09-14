package cs6301.g42;
public class Term implements Comparable<Term>
{
	private int coefficient;
	private int exponent;

	public Term(int coefficient, int exponent)
	{
		this.coefficient = coefficient;
		this.exponent = exponent;
	}

	public String toString()
	{
		return "(" + coefficient + ", " + exponent + ")";
	}

	public int compareTo(Term other)
	{
		if(this.exponent < other.exponent)
		{
			return -1;
		}
		else if(this.exponent > other.exponent) 
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}

	public void setCoefficient(int coefficient)
	{
		this.coefficient = coefficient;
	}

	public Term multiply(Term other)
	{
		int resultCoefficient, resultExponent;

		resultCoefficient = this.coefficient*other.coefficient;
		resultExponent = this.exponent + other.exponent;

		return new Term(resultCoefficient, resultExponent);
	}

	public int getCoefficient()
	{
		return coefficient;
	}

	public int getExponent()
	{
		return exponent;
	}

}