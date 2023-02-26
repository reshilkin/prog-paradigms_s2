package queue;

import java.util.function.Predicate;

public class LinkedQueue extends AbstractQueue {
    private Node first;
    private Node last;

    @Override
    protected void enqueueImpl(Object element) {
        Node node = new Node(null, element);
        if(isEmpty()) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
    }

    @Override
    public Object element() {
        return first.element;
    }

    @Override
    protected void dequeueImpl() {
        first = first.next;
    }

    @Override
    protected Queue makeQueue() {
        return new LinkedQueue();
    }

    private static class Node {
        private Node next;
        private final Object element;

        public Node(Node next, Object element) {
            this.next = next;
            this.element = element;
        }
    }
}
