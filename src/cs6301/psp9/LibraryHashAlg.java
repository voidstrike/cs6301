package cs6301.psp9;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cs6301.psp9.Graph.Vertex;
import cs6301.psp9.Graph.Edge;

/**
 * Created by Alan Lin on 11/9/2017.
 *
 */
public class LibraryHashAlg {
    // SP9 Q1
    static int howMany(int[] arr, int target){
        HashMap<Integer, Integer> auxMap = new HashMap<>();
        int pairNum = 0;
        for(int element : arr){
            Integer tmp = auxMap.get(element);
            if (tmp == null)
                auxMap.put(element, 1);
            else
                auxMap.replace(element, tmp+1);

            if (auxMap.get(target - element) != null)
                pairNum += auxMap.get(target-element);
        }
        return pairNum;
    }

    // SP9 Q2
    static<T extends Comparable<? super T>> T[] exactlyOnce(T[] arr){
        LinkedHashMap<T, Integer> auxMap = new LinkedHashMap<>();
        int resCount = 0;
        for (T element : arr){
            Integer tmp = auxMap.get(element);
            if (tmp == null) {
                auxMap.put(element, 1);
                resCount++;
            }
            else {
                auxMap.replace(element, tmp + 1);
                resCount -= tmp == 1 ? 1 : 0;
            }
        }
        if(resCount == 0)
            return null;

        @SuppressWarnings("unchecked")
        T[] solution = (T[]) Array.newInstance(arr.getClass().getComponentType(), resCount);
        int index = 0;
        for(T element : arr){
            Integer tmp = auxMap.get(element);
            if(tmp == 1)
                solution[index++] = element;
        }
        return solution;
    }

    // SP9 Q3
    static int longestStreak(int[] arr){
        HashMap<Integer, Integer> auxMap = new HashMap<>();
        Integer less, more;
        int max = 1, tmpMax;
        for(int element : arr){
            if (auxMap.get(element) != null)
                continue;

            less = auxMap.get(element-1);
            more = auxMap.get(element+1);
            tmpMax = 1;

            if (less == null && more == null)
                auxMap.put(element, 1);
            else if (less == null){
                tmpMax = more+1;
                auxMap.put(element, tmpMax);
                auxMap.replace(element+1, tmpMax);
            }
            else if (more == null){
                tmpMax = less+1;
                auxMap.put(element, tmpMax);
                auxMap.replace(element-1, tmpMax);
            }
            else{
                tmpMax = less + more + 1;
                auxMap.put(element, tmpMax);
                auxMap.replace(element-1, tmpMax);
                auxMap.replace(element+1, tmpMax);
            }
            max = tmpMax > max ? tmpMax : max;
        }
        return max;
    }

    // SP9 Q4
    static void compactGraph(Graph g){
        HashMap<nodePair, Integer> auxMap = new HashMap<>();
        nodePair tmp;
        Integer weight;
        for(Vertex u : g){
            for (Edge e : u){
                tmp = new nodePair(u, e.otherEnd(u));
                weight = auxMap.get(tmp);
                if (weight == null)
                    auxMap.put(tmp, e.weight);
                else{
                    if (weight > e.weight)
                        auxMap.replace(tmp, e.weight);
                }
            }
            u.adj.clear();
            u.revAdj.clear();
        }
        for (Map.Entry<nodePair, Integer> pair : auxMap.entrySet()){
            g.addEdge(pair.getKey().from, pair.getKey().to, pair.getValue());
        }
    }
    // Auxiliary class for Q4
    static class nodePair{
        Vertex from;
        Vertex to;

        nodePair(Vertex f, Vertex t){
            from = f;
            to = t;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null)
                return false;
            nodePair other = (nodePair) obj;
            return this.from.equals(other.from) && this.to.equals(other.to);
        }
    }

    public static void main(String[] args){
        int[] test = {1,7,9,4,1,7,4,8,7,1};
        System.out.println(longestStreak(test));
    }
}
