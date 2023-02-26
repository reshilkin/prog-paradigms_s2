package queue;

public class ArrayQueue extends AbstractQueue{
    private Object[] elements;
    private int beg;

    public ArrayQueue() {
        this.elements = new Object[2];
    }

    @Override
    protected void enqueueImpl(Object element) {
        ensureCapacity(1);
        elements[index(size)] = element;
    }

    @Override
    public Object element() {
        return elements[beg];
    }

    @Override
    protected void dequeueImpl() {
        elements[beg] = null;
        beg = next(beg);
    }

    @Override
    protected void clearImpl() {
        beg = 0;
        elements = new Object[2];
    }

    @Override
    protected Queue makeQueue() {
        return new ArrayQueue();
    }

    // push:
    //      Pred: element != null
    //      Post: n' = n + 1 && a'[1] = element && for i = 2..n' a'[i] = a[i - 1]
    public void push(Object element) {
        ensureCapacity(1);
        beg = prev(beg);
        elements[beg] = element;
        size++;
    }

    // peek:
    //      Pred: n >= 1
    //      Post: R = a[n] && immutable(a, n)

    public Object peek() {
        return elements[index(size - 1)];
    }

    // remove:
    //      Pred: n >= 1
    //      Post: R = a[n] && n' = n - 1 && immutable(a, n')

    public Object remove() {
        Object res = peek();
        elements[index(--size)] = null;
        return res;
    }

    // indexOf:
    //      Pred: true
    //      Post: (R == -1 && for i = 1..n a[i] != element)
    //      || (0 <= R < n && a[R + 1] == element && for i = 1..R a[i] != element)
    public int indexOf(Object element) {
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
    public int lastIndexOf(Object element) {
        for (int i = size - 1; i >= 0; i--) {
            if (element.equals(elements[index(i)])) {
                return i;
            }
        }
        return -1;
    }

    private int prev(int i) {
        return (i + elements.length - 1) % elements.length;
    }

    private int next(int i) {
        return (i + 1) % elements.length;
    }

    private int index(int i) {
        return (beg + i) % elements.length;
    }

    private void ensureCapacity(int num) {
        if (size() + num >= elements.length) {
            Object[] temp = new Object[2 * (num + elements.length)];
            System.arraycopy(elements, beg, temp, 0, elements.length - beg);
            System.arraycopy(elements, 0, temp, elements.length - beg, beg);
            elements = temp;
            beg = 0;
        }
    }
}
