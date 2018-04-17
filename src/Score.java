import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by Alan Lin on 11/16/2017.
 */
public class Score {
    public static void main(String[] args) throws FileNotFoundException {
        System.setIn(new FileInputStream("F:\\TestGraph\\score.txt"));
        java.util.Scanner in = new java.util.Scanner(System.in);
        double credit, score;
        double creditSum=0;
        double PointSum = 0.0, GPA;
        while(in.hasNextDouble()){
            credit = in.nextDouble();
            creditSum+=credit;
            score = in.nextDouble();
            PointSum += (score >= 85 ? 4.0f : (1.5 + (score-60)/10f)) * credit;
            System.out.println(credit + ":" + score);
        }
        System.out.println(creditSum);
        GPA = PointSum /  creditSum;
        System.out.println(GPA);
    }
}