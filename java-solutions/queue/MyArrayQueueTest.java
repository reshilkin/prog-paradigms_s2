package queue;

public class MyArrayQueueTest {
    public static void main(String[] args) {
        ArrayQueue aq = new ArrayQueue();
        for (int i = 0; i < 10; i++) {
            aq.enqueue(i);
            System.out.println(aq.size());
        }
        System.out.println();
        aq.clear();
        for (int i = 0; i < 10; i++) {
            aq.enqueue(i * 2);
            System.out.println(aq.size());
        }
        System.out.println();
        while (!aq.isEmpty()) {
            System.out.println(aq.dequeue() + " " + aq.size());
        }
    }
}
