package queue;

/*
    enqueue – добавить элемент в очередь;
    element – первый элемент в очереди;
    dequeue – удалить и вернуть первый элемент в очереди;
    size – текущий размер очереди;
    isEmpty – является ли очередь пустой;
    clear – удалить все элементы из очереди.

    model: [a_0, ..., a_{size - 1}] -- очередь

    inv: size >= 0 && для всех 0 <= i < size : a[i] != null

    pred: x != null
    post: size == size' + 1 && [a_0, ..., a_size', x] где 0 <= i < size' : a'_i == a_i
    enqueue(x)

    pred: isEmpty == false
    post: Res == a_0 && a -- не изменилось
    element()

    pred: isEmpty == false
    post: Res == a_0 && [a_1, ... , a_{size - 1}] где для всех 1 <= i < size : a_i == a'_i && size = size' - 1
    deque()

    pred: true
    post: (Res == size, где size -- текущие кол-во элементов в [a_0, ... , a_{size - 1}]
    size()

    pred: true
    post: Res == (size == 0 (size -- текущие кол-во элементов в очереди))
    isEmpty()

    pred: true
    post: size == 0 && a -- не содержит ни одного элемента
    clear()
*/

import java.util.Arrays;

public class ArrayQueueModule {
    private static final int DEFAULT_CAPACITY = 10;
    private static Object[] a = new Object[DEFAULT_CAPACITY];

    private static int head = 0;
    private static int size = 0;

    //pred: in != null
    //post: size == size' + 1 && [a_0, ..., a_size', in] где 0 <= i < size' : a'_i == a_i
    public static void enqueue(Object in) {
        assert in != null;
        ensureCapacity();
        a[(head + size) % a.length] = in;
        size++;
    }

    //pred: in != null
    //post: a_0 == in && a_1, ..., a_n == a'_0, ..., a'_n && size == size' + 1
    public static void push(Object in) {
        assert in != null;
        ensureCapacity();
        a[(head - 1 + a.length) % a.length] = in;
        head = (head - 1 + a.length) % a.length;
        size++;
    }

    //pred: size > 0
    //post: Res == a_n(последнее добавленное в конец значение)
    public static Object peek() {
        assert size > 0;
        return a[(head + size - 1 + a.length) % a.length];
    }

    //pred: size > 0
    //pred: Res == a_n && [a_0, ... , a_{n - 1}] где для всех 0 <= i < n: a_i == a'_i && size = size' - 1
    public static Object remove() {
        Object temp = a[(head + size - 1 + a.length) % a.length];
        a[(head + size - 1 + a.length) % a.length] = null;
        size--;
        return temp;
    }

    //pred: true
    //post: (Res == size, где size -- текущие кол-во элементов в [a_0, ... , a_{size - 1}]
    public static int size() {
        return size;
    }

    //pred: true
    //post: Res == (size == 0 (size -- текущие кол-во элементов в очереди))
    public static boolean isEmpty() {
        return (size == 0);
    }

    //pred: isEmpty == false
    //post: Res == a_0 && a -- не изменилось
    public static Object element() {
        return a[head];
    }

    //pred: isEmpty == false
    //post: Res == a_0 && [a_1, ... , a_{size - 1}] где для всех 1 <= i < size : a_i == a'_i && size = size' - 1
    public static Object dequeue() {
        assert !isEmpty();
        Object temp = a[head];
        a[head] = null;
        head++;
        head %= a.length;
        size--;
        return temp;
    }

    //pred: true
    //post: size == 0 && a -- не содержит ни одного элемента
    public static void clear() {
        if (a.length - head < size) {
            Arrays.fill(a, head, a.length, null);
            Arrays.fill(a, 0, (head + size) % a.length, null);
        } else {
            Arrays.fill(a, head, (size + head) % a.length, null);
        }
        head = 0;
        size = 0;
    }

    //pred: true
    //post: (size' == a.length && size == 2 * size' && a -- содержит все те же элементы, что и a',
    // но начиная с нулевого индекса) || (size' != a.length && ничего не изменилось)
    private static void ensureCapacity() {
        if (size != a.length) {
            return;
        }
        Object[] temp = new Object[2 * a.length];
        System.arraycopy(a, head, temp, 0, a.length - head);
        System.arraycopy(a, 0, temp, a.length - head, (head + size) % a.length);
        head = 0;
        a = temp;
    }
}
