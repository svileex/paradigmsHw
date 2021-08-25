package queue;

import java.util.Arrays;

public class ArrayQueue extends AbstractQueue {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[] a = new Object[DEFAULT_CAPACITY];

    private int head = 0;

    //pred: in != null
    //post: a_0 == in && a_1, ..., a_n == a'_0, ..., a'_n && size == size' + 1
    public void push(Object in) {
        assert in != null;
        ensureCapacity();
        a[(head - 1 + a.length) % a.length] = in;
        head = (head - 1 + a.length) % a.length;
        size++;
    }

    //pred: size > 0
    //post: Res == a_n(последнее добавленное в конец значение)
    public Object peek() {
        assert size > 0;
        return a[(head + size - 1 + a.length) % a.length];
    }

    //pred: size > 0
    //pred: Res == a_n && [a_0, ... , a_{n - 1}] где для всех 0 <= i < n: a_i == a'_i && size = size' - 1
    public Object remove() {
        Object temp = a[(head + size - 1 + a.length) % a.length];
        a[(head + size - 1 + a.length) % a.length] = null;
        size--;
        return temp;
    }

    @Override
    protected void implEnqueue(Object in) {
        ensureCapacity();
        a[(head + size) % a.length] = in;
    }

    @Override
    public Object element() {
        assert size != 0;
        return a[head];
    }

    @Override
    protected Object implDequeue() {
        Object temp = a[head];
        a[head] = null;
        head++;
        head %= a.length;
        return temp;
    }

    @Override
    public void clear() {
        Arrays.fill(a, null);
        size = head = 0;
    }

    //pred: true
    //post: (size' == a.length && size == 2 * size' && a -- содержит все те же элементы, что и a',
    // но начиная с нулевого индекса) || (size' != a.length && ничего не изменилось)
    private void ensureCapacity() {
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
