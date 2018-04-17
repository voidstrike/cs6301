package cs6301.personal;

/**
 * Created by Alan Lin on 9/6/2017.
 */
public class PolyNode {
    int coefficient, power;
    PolyNode next;

    PolyNode(int coefficient, int power, PolyNode nxt){
        this.coefficient = coefficient;
        this.power = power;
        this.next = nxt;
    }

    public void setCoefficient(int coefficient){
        this.coefficient = coefficient;
    }

    public void setPower(int power){
        this.power = power;
    }

    public void setNext(PolyNode nxt){
        this.next = nxt;
    }

}
