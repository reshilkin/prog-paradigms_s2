package queue;

import java.util.Arrays;

// Model:
//       a[1]..a[n],  for i = 1..n a[i] != null, n >= 0

// immutable(a, n) = for i = 1..n a'[i] = a[i]


public class ArrayQueueADT {
    private Object[] elements;
    private int beg;
    private int size;

    public ArrayQueueADT() {
        elements = new Object[2];
    }

    // enqueue:
    //       Pred: element != null && aq != null
    //       Post: n' = n + 1 && a[n'] = element && immutable(a, n)
    public static void enqueue(ArrayQueueADT aq, Object element) {
        ensureCapacity(aq, 1);
        aq.elements[index(aq, aq.size++)] = element;
    }

    // element:
    //       Pred: n >= 1 && aq != null
    //       Post: R = a[1] && for immutable(a, n)
    public static Object element(ArrayQueueADT aq) {
        return aq.elements[aq.beg];
    }

    // dequeue:
    //       Pred: n >= 1 && aq != null
    //       Post: n' = n - 1 && for i = 1..n' a'[i] = a[i + 1] && R = a[1]
    public static Object dequeue(ArrayQueueADT aq) {
        Object temp = aq.elements[aq.beg];
        aq.elements[aq.beg] = null;
        aq.beg = next(aq, aq.beg);
        aq.size--;
        return temp;
    }

    // size:
    //       Pred: true && aq != null
    //       Post: R = n && immutable(a, n)
    public static int size(ArrayQueueADT aq) {
        return aq.size;
    }

    // isEmpty:
    //       Pred: true && aq != null
    //       Post: R = (n == 0) && immutable(a, n)
    public static boolean isEmpty(ArrayQueueADT aq) {
        return size(aq) == 0;
    }

    // clear:
    //       Pred: true && aq != null
    //       Post: n = 0
    public static void clear(ArrayQueueADT aq) {
        aq.beg = 0;
        aq.size = 0;
        aq.elements = new Object[2];
    }

    // push:
    //      Pred: element != null && aq != null
    //      Post: n' = n + 1 && a'[1] = element && for i = 2..n' a'[i] = a[i - 1]
    public static void push(ArrayQueueADT aq, Object element) {
        ensureCapacity(aq, 1);
        aq.beg = prev(aq, aq.beg);
        aq.elements[aq.beg] = element;
        aq.size++;
    }

    // peek:
    //      Pred: n >= 1 && aq != null
    //      Post: R = a[n] && immutable(a, n)
    public static Object peek(ArrayQueueADT aq) {
        return aq.elements[index(aq, aq.size - 1)];
    }

    // remove:
    //      Pred: n >= 1 && aq != null
    //      Post: R = a[n] && n' = n - 1 && immutable(a, n')
    public static Object remove(ArrayQueueADT aq) {
        Object res = peek(aq);
        aq.elements[index(aq, --aq.size)] = null;
        return res;
    }

    // indexOf:
    //      Pred: aq != null
    //      Post: (R == -1 && for i = 1..n a[i] != element)
    //      || (0 <= R < n && a[R + 1] == element && for i = 1..R a[i] != element)
    public static int indexOf(ArrayQueueADT aq, Object element) {
        for (int i = 0; i < aq.size; i++) {
            if (element.equals(aq.elements[index(aq, i)])) {
                return i;
            }
        }
        return -1;
    }

    // lastIndexOf:
    //      Pred: aq != null
    //      Post: (R == -1 && for i = 1..n a[i] != element)
    //      || (0 <= R < n && a[R + 1] == element && for i = R + 2..n a[i] != element)
    public static int lastIndexOf(ArrayQueueADT aq, Object element) {
        for (int i = aq.size - 1; i >= 0; i--) {
            if (element.equals(aq.elements[index(aq, i)])) {
                return i;
            }
        }
        return -1;
    }

    private static int prev(ArrayQueueADT aq, int i) {
        return (i + aq.elements.length - 1) % aq.elements.length;
    }

    private static int next(ArrayQueueADT aq, int i) {
        return (i + 1) % aq.elements.length;
    }

    private static int index(ArrayQueueADT aq, int i) {
        return (aq.beg + i) % aq.elements.length;
    }

    private static void ensureCapacity(ArrayQueueADT aq, int num) {
        if (size(aq) + num >= aq.elements.length) {
            Object[] temp = new Object[2 * (num + aq.elements.length)];
            System.arraycopy(aq.elements, aq.beg, temp, 0, aq.elements.length - aq.beg);
            System.arraycopy(aq.elements, 0, temp, aq.elements.length - aq.beg, aq.beg);
            aq.elements = temp;
            aq.beg = 0;
        }
    }
}