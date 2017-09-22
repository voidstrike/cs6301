package cs6301.g42;

/**
 * Created by Alan Lin on 9/22/2017.
 * @Auther Khaled Al-naami, Peter Farago, Yu Lin, David Tan
 */
public class DLinkedList<T> {
    static class Entry<T>{
        T element;
        Entry<T> prev, next;

        Entry(T element, Entry<T> pre, Entry<T> nxt){
            this.element = element;
            this.prev = pre;
            this.next = nxt;
        }
    }

    Entry<T> head, tail;
    int size;

    public DLinkedList(){
        head = new Entry<>(null, null, null);
        tail = head;
        size = 0;
    }

    public void add(T target){
        tail.next = new Entry<>(target, tail, null);
        tail = tail.next;
        size++;
    }

    public DLinkedList.Entry sortedListToBST(DLinkedList.Entry head){
        // Return the root of BST
        //Initialize the variables
        DLinkedList.Entry middle = getMiddle(head);
        DLinkedList.Entry nextHead = middle.next;
        middle.next = null;

        if (head == middle){
            // Only one element this case
            head.next = null;
            head.prev = null;
        }
        else{
            middle.prev = toChild(head);
            middle.next = toChild(nextHead);
        }
        return middle;
    }

    public DLinkedList.Entry BSTToSortedList(DLinkedList.Entry root){
        Entry listHead;
        Entry tmp;
        if (root.next == null && root.prev == null){
            listHead = root;
        }
        else{
            tmp = buildList(root.prev, 0);
            root.prev = tmp;
            if (tmp!=null)
                tmp.next = root;
//            root.prev = buildList(root.prev, 0);
            tmp = buildList(root.next, 1);
            root.next = tmp;
            if (tmp!=null)
                tmp.prev = root;
//            root.next = buildList(root.next, 1);
            while(root.prev != null){
                root = root.prev;
            }
            listHead = root;
        }
        return listHead;
    }

    private DLinkedList.Entry toChild(DLinkedList.Entry head){
        if (head == null)
            return null;
        // P1--M--P2
        // getMiddle make P1.tail = null and return M
        DLinkedList.Entry middle = getMiddle(head);
        // Set nextHead = M.next, which equals P2.head
        DLinkedList.Entry nextHead = middle.next;
        middle.next = null;

        if (head == middle){
            // Only one element this case, make it a leaf node
            head.next = null;
            head.prev = null;
        }
        else{
            middle.prev = toChild(head);
            middle.next = toChild(nextHead);
        }
        return middle;
    }

    // Auxiliary methods
    private static DLinkedList.Entry getMiddle(DLinkedList.Entry startNode){
        DLinkedList.Entry firstTracker, secondTracker;
        if (startNode.next == null){
            // ONLY one element this case
            return startNode;
        }
        else{
            // MORE than one element
            firstTracker = startNode;
            secondTracker = startNode.next;
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
            return  secondTracker;
        }
    }

    private static Entry buildList(Entry node, int code){
        Entry tmp;
        if(node == null)
            return null;
        else{
            tmp = buildList(node.prev, 0);
            node.prev = tmp;
            if(tmp!=null)
                tmp.next = node;
//            node.prev = buildList(node.prev, 0);
           tmp = buildList(node.next, 1);
           node.next = tmp;
           if (tmp != null)
                tmp.prev = node;
        }
        tmp = node;
        if (code == 0){
            while(tmp.next != null) {
                tmp = tmp.next;
            }
        }
        else{
            while(tmp.prev != null) {
                tmp = tmp.prev;
            }
        }
        return tmp;
    }

    public static void main(String[] args){
        //Test code below
        DLinkedList<Integer> test = new DLinkedList<>();
        test.add(1);
        test.add(2);
        test.add(3);
        test.add(4);
        test.add(5);
        Entry result = test.sortedListToBST(test.head);
        Entry newList = test.BSTToSortedList(result);
        System.out.print(result.toString());
    }
}
