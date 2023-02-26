package queue;

import java.util.Arrays;

// Model:
//       a[1]..a[n],  for i = 1..n a[i] != null, n >= 0

// immutable(a, n) = for i = 1..n a'[i] = a[i]


public class ArrayQueueModule {
    private static Object[] elements = new Object[2];
    private static int beg;
    private static int size;

    // enqueue:
    //       Pred: element != null
    //       Post: n' = n + 1 && a[n'] = element && immutable(a, n)
    public static void enqueue(Object element) {
        ensureCapacity(1);
        elements[index(size++)] = element;
    }

    // element:
    //       Pred: n >= 1
    //       Post: R = a[1] && for immutable(a, n)
    public static Object element() {
        return elements[beg];
    }

    // dequeue:
    //       Pred: n >= 1
    //       Post: n' = n - 1 && for i = 1..n' a'[i] = a[i + 1] && R = a[1]
    public static Object dequeue() {
        Object temp = elements[beg];
        elements[beg] = null;
        beg = next(beg);
        size--;
        return temp;
    }

    // size:
    //       Pred: true
    //       Post: R = n && immutable(a, n)
    public static int size() {
        return size;
    }

    // isEmpty:
    //       Pred: true
    //       Post: R = (n == 0) && immutable(a, n)
    public static boolean isEmpty() {
        return size() == 0;
    }

    // clear:
    //       Pred: true
    //       Post: n = 0
    public static void clear() {
        beg = 0;
        size = 0;
        elements = new Object[2];
    }

    // push:
    //      Pred: element != null
    //      Post: n' = n + 1 && a'[1] = element && for i = 2..n' a'[i] = a[i - 1]
    public static void push(Object element) {
        ensureCapacity(1);
        beg = prev(beg);
        elements[beg] = element;
        size++;
    }

    // peek:
    //      Pred: n >= 1
    //      Post: R = a[n] && immutable(a, n)

    public static Object peek() {
        return elements[index(size - 1)];
    }

    // remove:
    //      Pred: n >= 1
    //      Post: R = a[n] && n' = n - 1 && immutable(a, n')

    public static Object remove() {
        Object res = peek();
        elements[index(--size)] = null;
        return res;
    }

    // indexOf:
    //      Pred: true
    //      Post: (R == -1 && for i = 1..n a[i] != element)
    //      || (0 <= R < n && a[R + 1] == element && for i = 1..R a[i] != element)
    public static int indexOf(Object element) {
        for (int i = 0; i < size; i++) {
            if (element.equals(elements[index(i)])) {
                return i;
            }
        }
        return -1;
    }

    // lastIndexOf:
    //      Pred: true
    //      Post: (R == -1 && for i = 1..n a[i] != element)
    //      || (0 <= R < n && a[R + 1] == element && for i = R + 2..n a[i] != element)
    public static int lastIndexOf(Object element) {
        for (int i = size - 1; i >= 0; i--) {
            if (element.equals(elements[index(i)])) {
                return i;
            }
        }
        return -1;
    }

    private static int prev(int i) {
        return (i + elements.length - 1) % elements.length;
    }

    private static int next(int i) {
        return (i + 1) % elements.length;
    }

    private static int index(int i) {
        return (beg + i) % elements.length;
    }

    private static void ensureCapacity(int num) {
        if (size() + num >= elements.length) {
            Object[] temp = new Object[2 * (num + elements.length)];
            System.arraycopy(elements, beg, temp, 0, elements.length - beg);
            System.arraycopy(elements, 0, temp, elements.length - beg, beg);
            elements = temp;
            beg = 0;
        }
    }
}
