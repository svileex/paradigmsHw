package queue;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class ArrayQueueADTTest {
    public static final int MAX_CAPACITY = 100;

    //если работает вставка то работает и удаление(одно без другого
    //не проверить) соотвественно работает и element
    public static boolean enqueueTest(final int n) {
        assert n > 0;

        Random getRandomNumber = new Random();
        Queue<Integer> secondQueue = new ArrayDeque<>();
        ArrayQueueADT myQueue = ArrayQueueADT.createQueue();

        for (int i = 0; i < n; i++) {
            int count = getRandomNumber.nextInt(MAX_CAPACITY);

            for (int j = 0; j < count; j++) {
                ArrayQueueADT.enqueue(myQueue, j);
                secondQueue.add(j);
            }

            for (int j = 0; j < count; j++) {
                int test1 = (int) ArrayQueueADT.dequeue(myQueue);
                int test2 = secondQueue.poll();

                if (test1 != test2) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean clearTest(final int n) {
        assert n > 0;

        ArrayQueueADT myQueue = ArrayQueueADT.createQueue();
        Random getRandomNumber = new Random();

        for (int i = 0; i < n; i++) {
            int count = getRandomNumber.nextInt(MAX_CAPACITY);

            for (int j = 0; j < count; j++) {
                ArrayQueueADT.enqueue(myQueue, i);
            }

            ArrayQueueADT.clear(myQueue);
            if (ArrayQueueADT.size(myQueue) != 0) {
                return false;
            }
        }
        return true;
    }

    //если работает size, то и работает isEmpty
    public static boolean sizeTest(int n) {
        assert n > 0;

        ArrayQueueADT myQueue = ArrayQueueADT.createQueue();
        Random getRandomNumber = new Random();

        for (int i = 0; i < n; i++) {
            int count = getRandomNumber.nextInt(MAX_CAPACITY);
            int size = 0;

            for (size = 0; size < count; size++) {
                ArrayQueueADT.enqueue(myQueue, Integer.toString(size));
            }

            if (size != ArrayQueueADT.size(myQueue)) {
                return false;
            }

            ArrayQueueADT.clear(myQueue);
        }
        return true;
    }

    //проверяю push и remove если работает remove, то работает и peek
    public static boolean pushTest(int n) {
        assert n > 0;

        Random getRandomNumber = new Random();
        ArrayDeque<Integer> secondQueue = new ArrayDeque<>();
        ArrayQueueADT myQueue = ArrayQueueADT.createQueue();

        for (int i = 0; i < n; i++) {
            int count = getRandomNumber.nextInt(MAX_CAPACITY);

            for (int j = 0; j < count; j++) {
                ArrayQueueADT.push(myQueue, j);
                secondQueue.addFirst(j);
            }

            for (int j = 0; j < count; j++) {
                int test1 = (int) ArrayQueueADT.remove(myQueue);
                int test2 = secondQueue.removeLast();

                if (test1 != test2) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(enqueueTest(100));
        System.out.println(clearTest(100));
        System.out.println(sizeTest(100));
        System.out.println(pushTest(100));
    }
}
