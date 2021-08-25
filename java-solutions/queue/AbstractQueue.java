package queue;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    abstract protected void implEnqueue(Object in);
    abstract protected Object implDequeue();
    abstract public Object element();
    abstract public void clear();

    private boolean containsWithDelete(Object element, boolean deleteFlag) {
        Object temp;
        boolean flag = false;
        int k = size;
        for (int i = 0; i < k; i++) {
            temp = dequeue();
            if (element.equals(temp) && !flag) {
                flag = true;
                if (deleteFlag) {
                    continue;
                }
            }
            enqueue(temp);
        }
        return flag;
    }

    @Override
    public boolean removeFirstOccurrence(Object element) {
        return containsWithDelete(element, true);
    }

    @Override
    public boolean contains(Object element) {
        return containsWithDelete(element, false);
    }

    @Override
    public void enqueue(Object in) {
        assert in != null;

        implEnqueue(in);
        size++;
    }

    @Override
    public Object dequeue() {
        assert size > 0;

        Object temp =  implDequeue();
        size--;
        return temp;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

}
