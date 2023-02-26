package queue;

public class ArrayQueueADTTest {
    public static void main(String[] args) {
        ArrayQueueADT aq = new ArrayQueueADT();
        for (int i = 0; i < 10; i++) {
            ArrayQueueADT.enqueue(aq, i);
            System.out.println(ArrayQueueADT.size(aq));
        }
        System.out.println();
        ArrayQueueADT.clear(aq);
        for (int i = 0; i < 10; i++) {
            ArrayQueueADT.enqueue(aq, i * 2);
            System.out.println(ArrayQueueADT.size(aq));
        }
        System.out.println();
        while (!ArrayQueueADT.isEmpty(aq)) {
            System.out.println(ArrayQueueADT.dequeue(aq) + " " + ArrayQueueADT.size(aq));
        }
    }
}
