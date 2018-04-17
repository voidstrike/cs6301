package cs6301.personal;


import java.util.Iterator;
import java.util.ArrayDeque;

/**
 * Created by Alan Lin on 9/4/2017.
 */
public class SortableList<T extends Comparable<? super T>> extends SinglyLinkedList<T> {
    // Constructor
    public SortableList(){
        // Basically the same class as SinglyLinkedList<T>, so just call the constructor of it's parent
        super();
    }
    // get middle
    public static SortableList.Entry pGetMiddle(SortableList.Entry testNode){
        SortableList.Entry firstTracker;
        SortableList.Entry secondTracker;
        if (testNode.next == null)
            return testNode; // return testNode it self if testNode has no next Entry
        else if (testNode.next.next == null){
            secondTracker = testNode.next;
            testNode.next = null;
            return secondTracker; // return secondNode if testNode have only one next Entry
        }
        else{
            firstTracker = testNode.next;
            secondTracker = testNode.next.next;
            while(true){
                if (secondTracker.next == null || secondTracker.next.next == null){
                    break;
                }
                else{
                    firstTracker = firstTracker.next;
                    secondTracker = secondTracker.next.next;
                }
            }
            secondTracker = firstTracker.next;
            firstTracker.next = null;
            return secondTracker;
        }
    }

    public SortableList.Entry pMerge(SortableList.Entry firstNode, SortableList.Entry secondNode){
        if (firstNode == null)
            return secondNode;
        if (secondNode == null)
            return firstNode;

        T currentElement = (T) firstNode.element; // Initialize current value of the head of first linked list
        T compareElement = (T) secondNode.element; // Initialize current value of the head of second linked list
        T interElement;

        SortableList.Entry tmp;
        SortableList.Entry tmpHolder = currentElement.compareTo(compareElement) <= 0 ? firstNode : secondNode;

        while(true){
                if(currentElement.compareTo(compareElement) <=0){
                    tmp = firstNode.next;
                    if (tmp == null){
                        // run out of elements in first linked list, connected its tail.next to second linked list
                        firstNode.next = secondNode;
                        break;
                    }
                    else{
                        interElement = (T) tmp.element; // first linked list has next element
                        if (interElement.compareTo(compareElement) > 0)
                            firstNode.next = secondNode; // connect firstNode.next to second array if it's next element has bigger value
                        firstNode = tmp; // Update first linked list
                    }
                    currentElement = (T) firstNode.element;
                }
                else{
                    tmp = secondNode.next;
                    if (tmp == null){
                        secondNode.next = firstNode;
                        break;
                    }
                    else{
                        // symetric process of first linked list
                        interElement = (T) tmp.element;
                        if (interElement.compareTo(currentElement) > 0)
                            secondNode.next = firstNode;
                        secondNode = tmp;
                    }
                    compareElement = (T) secondNode.element;
                }
        }
        assert firstNode.next == null || secondNode.next == null;
        // Since we moved all the linked list, we don't need to check whether one of the list is empty
        return tmpHolder;
    }

    private SortableList.Entry pMergeSort(SortableList.Entry firstNode){

        SortableList.Entry middle = pGetMiddle(firstNode);

        if (middle == firstNode)
            return firstNode;
        else{
            firstNode = pMergeSort(firstNode);
            middle = pMergeSort(middle);
            firstNode = pMerge(firstNode, middle);
        }
        return  firstNode;
    }

    void mergeSort(){
        Iterator tmp = this.iterator();
        if (tmp.hasNext()){
            head.next = this.pMergeSort(this.head.next);
        }
    }

    public static <T extends  Comparable<? super T>>
        void mergeSort(SortableList<T> list){
        list.mergeSort();
    }

    static class InfoNode{
//            boolean firstPartDone = false, secondPartDone = false;
            SortableList.Entry firstPart = null, secondPart = null;
            SortableList.Entry resultNode = null;
            int phaseNum;

            InfoNode(SortableList.Entry pFirstPart, SortableList.Entry pSecondePart){
                this.firstPart = pFirstPart;
                this.secondPart = pSecondePart;
                this.phaseNum = 0;
            }

    }

    public static SortableList.Entry stackSimulateMergeSort(SortableList.Entry startNode){
            ArrayDeque<InfoNode> operatingStack = new ArrayDeque<>();
            SortableList.Entry middle = pGetMiddle(startNode);
            InfoNode currentInfoNode=null, resultNode=null;
            operatingStack.push(new InfoNode(startNode, middle));
            SortableList helper = new SortableList();

            // Simulating the behavior of stack while doing recursion
            while (!operatingStack.isEmpty()){
                currentInfoNode = operatingStack.peekFirst();
                if (currentInfoNode.firstPart == currentInfoNode.secondPart){
                    // Only one elements in this case
                    currentInfoNode.resultNode = currentInfoNode.firstPart;
                    resultNode = operatingStack.pop();
                }
                else{
                    // There are more than two elements in those cases
                    if (currentInfoNode.phaseNum == 0){
                        // First phase in this function, which means before we execute first part
                        currentInfoNode.phaseNum = 1;
                        operatingStack.push(new InfoNode(currentInfoNode.firstPart, pGetMiddle(currentInfoNode.firstPart)));
                    }
                    else if (currentInfoNode.phaseNum == 1){
                        // Second phase in this function, which means we executed first recursive function
                        currentInfoNode.phaseNum = 2;
                        currentInfoNode.firstPart = resultNode.resultNode;
                        operatingStack.push(new InfoNode(currentInfoNode.secondPart, pGetMiddle(currentInfoNode.secondPart)));
                    }
                    else if (currentInfoNode.phaseNum == 2){
                        currentInfoNode.phaseNum = 3;
                        currentInfoNode.secondPart = resultNode.resultNode;
                        currentInfoNode.resultNode = helper.pMerge(currentInfoNode.firstPart, currentInfoNode.secondPart);
                        resultNode = operatingStack.pop();
                    }
                }
            }
            return resultNode.resultNode;
    }

    public static void wrappedStackSimulateMS(SortableList target){
        Iterator tmp = target.iterator();
        if (tmp.hasNext()){
            target.head.next = stackSimulateMergeSort(target.head.next);
        }
    }

    public static void main(String[] args){
        // Test code below
        SortableList<Integer> test = new SortableList<>();
        test.add(new Integer(12));
        test.add(new Integer(2));
        test.add(new Integer(15));
        test.add(new Integer(1));
        test.add(new Integer(123));
        test.add(new Integer(124));

        //mergeSort(test);
        wrappedStackSimulateMS(test);

        Iterator tmp = test.iterator();
        while(tmp.hasNext()){
            System.out.print(tmp.next().toString()+ " ");
        }
    }
}
