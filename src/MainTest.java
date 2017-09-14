import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alan Lin on 8/24/2017.
 */
public class MainTest {
    public static void main(String args[]){
        ArrayList<Integer> newList = new ArrayList();
        newList.add(3);
        ArrayList<Integer> newList2 = new ArrayList();
        newList2.add(3);
        ArrayList<Integer> test = new ArrayList();
        LinkListSort sorter = new LinkListSort();
        sorter.intersect(newList, newList2, test);
        System.out.print(test);
//        int i = 1;
//        for(Iterator iter = newList.iterator();iter.hasNext();){
//            System.out.println(i +"  " + iter.next().toString());
//        }
    }
}
