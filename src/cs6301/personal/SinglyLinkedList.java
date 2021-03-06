/** @author rbk
 *  Singly linked list: for instructional purposes only
 *  Ver 1.0: 2017/08/08
 *  Ver 1.1: 2017/08/30: Fixed error: If last element of list is removed,
 *  "tail" is no longer a valid value.  Subsequently, if items are added
 *  to the list, code would do the wrong thing.
 */

package cs6301.personal;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SinglyLinkedList<T> implements Iterable<T> {

	/**
	 * Class Entry holds a single node of the list
	 */
	static class Entry<T> {
		T element;
		Entry<T> next;

		Entry(T x, Entry<T> nxt) {
			element = x;
			next = nxt;
		}
	}

	// Dummy header is used.  tail stores reference of tail element of list
	Entry<T> head, tail;
	int size;

	public SinglyLinkedList() {
		head = new Entry<>(null, null);
		tail = head;
		size = 0;
	}

	public Iterator<T> iterator() {
		return new SLLIterator<>(this);
	}

	private class SLLIterator<E> implements Iterator<E> {
		SinglyLinkedList<E> list;
		Entry<E> cursor, prev;
		boolean ready;  // is item ready to be removed?

		SLLIterator(SinglyLinkedList<E> list) {
			this.list = list;
			cursor = list.head;
			prev = null;
			ready = false;
		}

		public boolean hasNext() {
			return cursor.next != null;
		}

		public E next() {
			prev = cursor;
			cursor = cursor.next;
			ready = true;
			return cursor.element;
		}

		// Removes the current element (retrieved by the most recent next())
		// Remove can be called only if next has been called and the element has not been removed
		public void remove() {
			if (!ready) {
				throw new NoSuchElementException();
			}
			prev.next = cursor.next;
			// Handle case when tail of a list is deleted
			if (cursor == list.tail) {
				list.tail = prev;
			}
			cursor = prev;
			ready = false;  // Calling remove again without calling next will result in exception thrown
			size--;
		}
	}

	// Add new elements to the end of the list
	public void add(T x) {
		tail.next = new Entry<>(x, null);
		tail = tail.next;
		size++;
	}

	public void printList() {
	/* Code without using implicit iterator in for each loop:

        Entry<T> x = head.next;
        while(x != null) {
            System.out.print(x.element + " ");
            x = x.next;
        }
	*/

		System.out.print(this.size + ": ");
		for (T item : this) {
			System.out.print(item + " ");
		}

		System.out.println();
	}

	// Rearrange the elements of the list by linking the elements at even index
	// followed by the elements at odd index. Implemented by rearranging pointers
	// of existing elements without allocating any new elements.
	public void unzip() {
		if (size < 3) {  // Too few elements.  No change.
			return;
		}

		Entry<T> tail0 = head.next;
		Entry<T> head1 = tail0.next;
		Entry<T> tail1 = head1;
		Entry<T> c = tail1.next;
		int state = 0;

		// Invariant: tail0 is the tail of the chain of elements with even index.
		// tail1 is the tail of odd index chain.
		// c is current element to be processed.
		// state indicates the state of the finite state machine
		// state = i indicates that the current element is added after taili (i=0,1).
		while (c != null) {
			if (state == 0) {
				tail0.next = c;
				tail0 = c;
				c = c.next;
			} else {
				tail1.next = c;
				tail1 = c;
				c = c.next;
			}
			state = 1 - state;
		}
		tail0.next = head1;
		tail1.next = null;
	}

	// Below is the code for SP2 Q3
	public void multiUnzip(int zipCode) {
		if (size < zipCode + 1){
			return; // Too few elements, no change
		}
		Entry<T> currentElement = head.next;
		Entry<T>[] entryList = new Entry[zipCode]; // head list
		Entry<T>[] entryTails = new Entry[zipCode]; // tail list
		for (int i=0; i< zipCode; i++){
			// Initialize head and tail list
			entryList[i] = currentElement;
			entryTails[i] = currentElement;
			currentElement = currentElement.next;
		}
		int count = 0;
		while(currentElement != null){
			// Update each element in tail list if possible
			entryTails[count % zipCode].next = currentElement;
			entryTails[count % zipCode] = entryTails[count % zipCode].next;
			currentElement = currentElement.next;
			count++;
		}
		for(int i=1; i<zipCode; i++){
			entryTails[i-1].next = entryList[i];
		}
		entryTails[zipCode - 1].next = null;
	}

	// Below is the code for SP2 Q4
	private Entry reverseIter(Entry node) {
		//TODO
		Entry last = null, current;
		while (node.next != null) {
			current = node;
			node = node.next;
			current.next = last;
			last = current;
		}
		node.next = last;
		this.head.next = node;
		return node;
	}

	private Entry reverseRec(Entry node) {
		if (node.next == null){
			this.head.next = node;
			return node;
		}
		else {
			Entry tmp = reverseRec(node.next);
			tmp.next = node;
			node.next = null;
			return node;
		}
	}

	private void printRevIter(Entry node) {
		// Since we just want print the elements in reverse order without touch the elements itself
		// we will not call RevIter() here and we use a array-based stack to implement that;
		int count = 0;
		Entry[] reverse = new Entry[size];
		while(node.next!=null){
			// push Entry into this stack if it's next isn't null
			reverse[count++] = node;
			node = node.next;
		}
		reverse[count] = node;
		for(int i=count; i>=0; i--){
			System.out.println(reverse[i].element.toString());
		}
	}

	private void printRevRec(Entry node) {
		if (node.next == null) {
			System.out.println(node.element.toString());
		} else {
			printRevRec(node.next);
			System.out.println(node.element.toString());
		}
	}

	public static void recPrintRev(SinglyLinkedList inputList){
		// Wrap static method to call printRevRec();
		if (inputList.head.next != null)
			inputList.printRevRec(inputList.head.next);
	}

	public static void recRev(SinglyLinkedList inputList){
		// Wrap static method to call reverseRec();
		if (inputList.head.next != null){
			// we will passing head.next since head is a dumpy node;
			inputList.reverseRec(inputList.head.next);
		}
	}

	public static void iterPrintRev(SinglyLinkedList inputList){
		// Wrap method for printRevIter();
		if(inputList.head.next != null)
			inputList.printRevIter(inputList.head.next);
	}

	public static void iterRev(SinglyLinkedList inputList){
		// Wrap method of reverseIter();
		if (inputList.head.next != null){
			inputList.head.next = inputList.reverseIter(inputList.head.next);
		}
	}

	public static void main(String[] args) throws NoSuchElementException {
		int n = 10;
		if (args.length > 0) {
			n = Integer.parseInt(args[0]);
		}

		SinglyLinkedList<Integer> lst = new SinglyLinkedList<>();
		for (int i = 1; i <= 10; i++) {
			lst.add(new Integer(i));
		}
		lst.printList();
//		iterRev(lst);
//		iterPrintRev(lst);
//		lst.printList();

		//lst.unzip();
		lst.multiUnzip(4);
		lst.printList();

		Iterator<Integer> it = lst.iterator();
		Scanner in = new Scanner(System.in);
	}
}
//	whileloop:
//	while(in.hasNext()) {
//	    int com = in.nextInt();
//	    switch(com) {
//	    case 1:  // Move to next element and print it
//		if (it.hasNext()) {
//		    System.out.println(it.next());
//		} else {
//		    break whileloop;
//		}
//		break;
//	    case 2:  // Remove element
//		it.remove();
//		lst.printList();
//		break;
//	    default:  // Exit loop
//		 break whileloop;
//	    }
//	}
//	lst.printList();
//	lst.unzip();
//        lst.printList();
//    }

/* Sample input:
   1 2 1 2 1 1 1 2 1 1 2 0
   Sample output:
10: 1 2 3 4 5 6 7 8 9 10 
1
9: 2 3 4 5 6 7 8 9 10 
2
8: 3 4 5 6 7 8 9 10 
3
4
5
7: 3 4 6 7 8 9 10 
6
7
6: 3 4 6 8 9 10 
6: 3 4 6 8 9 10 
6: 3 6 9 4 8 10
*/
