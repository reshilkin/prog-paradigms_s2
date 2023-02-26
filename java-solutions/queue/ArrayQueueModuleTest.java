package queue;

public class ArrayQueueModuleTest {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            ArrayQueueModule.enqueue(i);
            System.out.println(ArrayQueueModule.size());
        }
        System.out.println();
        ArrayQueueModule.clear();
        for (int i = 0; i < 10; i++) {
            ArrayQueueModule.enqueue(i * 2);
            System.out.println(ArrayQueueModule.size());
        }
        System.out.println();
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println(ArrayQueueModule.dequeue() + " " + ArrayQueueModule.size());
        }
    }
}
