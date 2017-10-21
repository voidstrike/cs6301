import java.util.*;
import java.lang.reflect.Array;

public class SP7Problem8
{

    public static int longestStreak(int[] a) 
    {

    	TreeSet<Integer> treeSet = new TreeSet<Integer>();

    	for(int currentInt: a)
    	{
    		treeSet.add(currentInt);
    	}

    	int lowestInt, currentStreak = 1, longestStreak = 0;
    	int previousInt = treeSet.pollFirst();

    	while(!treeSet.isEmpty())
    	{
    		lowestInt = treeSet.pollFirst();

    		if(lowestInt == previousInt + 1)
    		{
    			currentStreak++;
    			previousInt = lowestInt;
    		}
    		else
    		{
    			if(currentStreak > longestStreak)
    			{
    				longestStreak = currentStreak;
    			}
    	
    			previousInt = lowestInt;
    			currentStreak = 1;
    			
    		}

    	}

    	if(currentStreak > longestStreak)
		{
			longestStreak = currentStreak;
		}

    	return longestStreak;
   	}

	public static void main(String[] args)
	{
		int[] a = {4,5,6,2,8,0,9,1};
		System.out.println(longestStreak(a));
	}

}
