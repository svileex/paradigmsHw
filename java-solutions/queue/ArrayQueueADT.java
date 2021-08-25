package queue;

//model: [a_0, ..., a_{size - 1}] -- очередь
//inv: size >= 0 && для всех 0 <= i < size : a[i] != null

import java.util.Arrays;

public class ArrayQueueADT {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] a = new Object[DEFAULT_CAPACITY];

    private int head = 0;
    private int size = 0;

    //pred: in != null && queue != null
    //post: a_0 == in && a_1, ..., a_n == a'_0, ..., a'_n && size == size' + 1
    public static void push(ArrayQueueADT queue, Object in) {
        assert in != null;
        ensureCapacity(queue);
        queue.a[(queue.head - 1 + queue.a.length) % queue.a.length] = in;
        queue.head = (queue.head - 1 + queue.a.length) % queue.a.length;
        queue.size++;
    }

    //pred: size > 0 && queue != null
    //post: Res == a_n(последнее добавленное в конец значение)
    public static Object peek(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.a[(queue.head + queue.size - 1 + queue.a.length) % queue.a.length];
    }

    //pred: size > 0 && queue != null
    //pred: Res == a_n && [a_0, ... , a_{n - 1}] где для всех 0 <= i < n: a_i == a'_i && size = size' - 1
    public static Object remove(ArrayQueueADT queue) {
        Object temp = queue.a[((queue.head + queue.size) % queue.a.length - 1 + queue.a.length) % queue.a.length];
        queue.a[(queue.head + queue.size - 1 + queue.a.length) % queue.a.length] = null;
        queue.size--;
        return temp;
    }

    //pred: true
    //post: Res == new ArrayQueueADT
    public static ArrayQueueADT createQueue() {
        return new ArrayQueueADT();
    }

    //pred: in != null && queue != null
    //post: size == size' + 1 && [a_0, ..., a_size', in] где 0 <= i < size' : a'_i == a_i
    public static void enqueue(ArrayQueueADT queue, Object in) {
        assert in != null;
        ensureCapacity(queue);
        queue.a[(queue.head + queue.size) % queue.a.length] = in;
        queue.size++;
    }

    //pred: queue != null
    //post: (Res == size, где size -- текущие кол-во элементов в [a_0, ... , a_{size - 1}]
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    //pred: queue != null
    //post: Res == (size == 0 (size -- текущие кол-во элементов в очереди))
    public static boolean isEmpty(ArrayQueueADT queue) {
        return (queue.size == 0);
    }

    //pred: isEmpty == false && queue != null
    //post: Res == a_0 && a -- не изменилось
    public static Object element(ArrayQueueADT queue) {
        return queue.a[queue.head];
    }

    //pred: isEmpty == false && queue != null
    //post: Res == a_0 && [a_1, ... , a_{size - 1}] где для всех 1 <= i < size : a_i == a'_i && size = size' - 1
    public static Object dequeue(ArrayQueueADT queue) {
        assert !isEmpty(queue);
        Object temp = queue.a[queue.head];
        queue.a[queue.head] = null;
        queue.head++;
        queue.head %= queue.a.length;
        queue.size--;
        return temp;
    }

    //pred: queue != null
    //post: size == 0 && a -- не содержит ни одного элемента
    public static void clear(ArrayQueueADT queue) {
        if (queue.a.length - queue.head < queue.size) {
            Arrays.fill(queue.a, queue.head, queue.a.length, null);
            Arrays.fill(queue.a, 0, (queue.head + queue.size) % queue.a.length, null);
        } else {
            Arrays.fill(queue.a, queue.head, (queue.size + queue.head) % queue.a.length, null);
        }
        queue.head = 0;
        queue.size = 0;
    }

    //pred: queue != null
    //post: (size' == a.length && size == 2 * size' && a -- содержит все те же элементы, что и a',
    // но начиная с нулевого индекса) || (size' != a.length && ничего не изменилось)
    private static void ensureCapacity(ArrayQueueADT queue) {
        if (queue.size != queue.a.length) {
            return;
        }
        Object[] temp = new Object[2 * queue.a.length];
        System.arraycopy(queue.a, queue.head, temp, 0, queue.a.length - queue.head);
        System.arraycopy(queue.a, 0, temp, queue.a.length - queue.head, (queue.head + queue.size) % queue.a.length);
        queue.head = 0;
        queue.a = temp;
    }
}