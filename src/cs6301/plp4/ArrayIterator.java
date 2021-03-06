/**  Iterator for arrays
 *   @author rbk
 *  Ver 1.0: 2017/08/08
 */

package cs6301.plp4;

import java.util.Iterator;

public class ArrayIterator<T> implements Iterator<T> {
    private T[] arr;
    private int startIndex, endIndex, cursor;

    ArrayIterator(T[] a) {
	    arr = a;
	    startIndex = 0;
	    endIndex = a.length-1;
	    cursor = -1;
    }

    ArrayIterator(T[] a, int start, int end) {
	    arr = a;
	    startIndex = start;
	    endIndex = end;
	    cursor = start - 1;
    }

    public boolean hasNext() {
	return cursor < endIndex;
    }

    public T next() {
	return arr[++cursor];
    }

    public void remove() {
	throw new UnsupportedOperationException();
    }
}
