package queue;

public class LinkedQueue extends AbstractQueue {
    private Node head = null;
    private Node tail = null;

    @Override
    protected void implEnqueue(Object in) {
        Node temp = new Node(in, null);
        if (tail != null) {
            tail.next = temp;
        } else {
            head = temp;
        }
        tail = temp;
    }

    @Override
    public Object element() {
        assert size != 0;
        return head.data;
    }

    @Override
    protected Object implDequeue() {
        Object temp = head.data;
        if (size > 1) {
            head = head.next;
        } else {
            tail = head = null;
        }
        return temp;
    }

    @Override
    public void clear() {
        tail = head = null;
        size = 0;
    }

    private static class Node {
        private final Object data;
        private Node next;

        public Node(Object element, Node next) {
            data = element;
            this.next = next;
        }
    }
}
