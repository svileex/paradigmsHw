package queue;

//model: [a_0, ..., a_{size - 1}] -- очередь
//inv: size >= 0 && для всех 0 <= i < size : a[i] != null

public interface Queue {

    //pred: x != null
    //post: size == size' + 1 && [a_0, ..., a_size', x] где 0 <= i < size' : a'_i == a_i
    void enqueue(Object element);

    //pred: true
    //post: (Res == size, где size -- текущие кол-во элементов в [a_0, ... , a_{size - 1}]
    int size();

    //pred: true
    //post: Res == (size == 0 (size -- текущие кол-во элементов в очереди))
    boolean isEmpty();

    //pred: isEmpty == false
    //post: Res == a_0 && a -- не изменилось
    Object element();

    //pred: isEmpty == false
    //post: Res == a_0 && [a_1, ... , a_{size - 1}] где для всех 1 <= i < size : a_i == a'_i && size = size' - 1
    Object dequeue();

    //pred: true
    //post: size == 0 && a -- не содержит ни одного элемента
    void clear();

    //pred: true
    //post: let a[size] = element, r = contains(element)
    // (i = min{0 <= i <= size | a_i == element} && forall 0 <= j < i : a_j = a'_j) && (forall size - 1 >= j > i : a_{j - 1} = a'j)
    //&& ((r == true && size == size' - 1) || (r == false && size == size'))
    boolean removeFirstOccurrence(Object element);

    //pred: true
    //post: Res == (существует i : a_i == element) && a -- не изменится
    boolean contains(Object element);

}
