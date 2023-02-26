package queue;

import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size;

    @Override
    public void enqueue(Object element) {
        enqueueImpl(element);
        size++;
    }

    protected void enqueueImpl(Object element) {
    }

    @Override
    public Object dequeue() {
        Object res = element();
        dequeueImpl();
        size--;
        return res;
    }

    protected void dequeueImpl() {
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void clear() {
        clearImpl();
        size = 0;
    }

    protected void clearImpl() {
    }

    @Override
    public void removeIf(Predicate<Object> predicate) {
        retainIf(predicate.negate());
    }

    @Override
    public void retainIf(Predicate<Object> predicate) {
        Queue queue = makeQueue();
        while (!isEmpty()) {
            if (predicate.test(element())) {
                queue.enqueue(element());
            }
            dequeue();
        }
        copy(queue);
    }

    @Override
    public void takeWhile(Predicate<Object> predicate) {
        copy(whileImpl(predicate, true));
    }

    @Override
    public void dropWhile(Predicate<Object> predicate) {
        copy(whileImpl(predicate, false));
    }

    private Queue whileImpl(Predicate<Object> predicate, boolean first) {
        Queue pref = makeQueue();
        Queue suf = makeQueue();
        while (!isEmpty() && predicate.test(element())) {
            pref.enqueue(dequeue());
        }
        while (!isEmpty()) {
            suf.enqueue(dequeue());
        }
        return first ? pref : suf;
    }

    private void copy(Queue queue) {
        while (!queue.isEmpty()) {
            enqueue(queue.dequeue());
        }
    }

    abstract Queue makeQueue();
}
