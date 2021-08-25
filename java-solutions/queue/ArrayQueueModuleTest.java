package queue;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class ArrayQueueModuleTest {

    public static final int MAX_CAPACITY = 100;

    //если работает вставка то работает и удаление(одно без другого
    //не проверить) соотвественно работает и element
    public static boolean enqueueTest(final int n) {
        assert n > 0;
        ArrayQueueModule.clear();

        Random getRandomNumber = new Random();
        Queue<Integer> secondQueue = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            int count = getRandomNumber.nextInt(MAX_CAPACITY);

            for (int j = 0; j < count; j++) {
                ArrayQueueModule.enqueue(j);
                secondQueue.add(j);
            }

            for (int j = 0; j < count; j++) {
                int test1 = (int) ArrayQueueModule.dequeue();
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
        ArrayQueueModule.clear();

        Random getRandomNumber = new Random();

        for (int i = 0; i < n; i++) {
            int count = getRandomNumber.nextInt(MAX_CAPACITY);

            for (int j = 0; j < count; j++) {
                ArrayQueueModule.enqueue(i);
            }

            ArrayQueueModule.clear();
            if (ArrayQueueModule.size() != 0) {
                return false;
            }
        }
        return true;
    }

    //если работает size, то и работает isEmpty
    public static boolean sizeTest(int n) {
        assert n > 0;
        ArrayQueueModule.clear();

        Random getRandomNumber = new Random();

        for (int i = 0; i < n; i++) {
            int count = getRandomNumber.nextInt(MAX_CAPACITY);
            int size = 0;

            for (size = 0; size < count; size++) {
                ArrayQueueModule.enqueue(Integer.toString(size));
            }

            if (size != ArrayQueueModule.size()) {
                return false;
            }

            ArrayQueueModule.clear();
        }
        return true;
    }

    //проверяю push и remove если работает remove, то работает и peek
    public static boolean pushTest(int n) {
        assert n > 0;
        ArrayQueueModule.clear();

        Random getRandomNumber = new Random();
        ArrayDeque<Integer> secondQueue = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            int count = getRandomNumber.nextInt(MAX_CAPACITY);

            for (int j = 0; j < count; j++) {
                ArrayQueueModule.push(j);
                secondQueue.addFirst(j);
            }

            for (int j = 0; j < count; j++) {
                int test1 = (int) ArrayQueueModule.remove();
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
