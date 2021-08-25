package search;


public class BinarySearchSpan {

    //abc: l < Res <= r && r' - l' < r - l && l, r целые числа в диапазоне от -1 до array.length (просто условие с названием abc)
    //pred: array != null && array[i] >= array[j] для любого i <= j && -1 <= l && r <= array.length && abc
    //назову это условие ** все кроме && abc
    //post: Res : (array[Res] <= x && (array[Res - 1] > x || Res == 0))
    //|| (Res == array.length && array[array.length - 1] > x)
    public static int recursiveLeftBinarySearch(final int[] array, int x, int l, int r) {
        //pred: abc && **
        if (r <= 1 + l) {
            // r > l (из abc) && r - l <= 1 => r - l == 1
            // r - l == 1 && abc && ** => Res == r
            return r; // выполнили контракт
        } else {
            // abc && ** && r - l > 1
            //pred: r - l > 1
            int m = l + (r - l) / 2;
            //post: m == l + (r - l) / 2
            //abc && ** && r - l > 1 && m == l + (r - l) / 2 => l < m < r
            //pred: abc && ** && r - l > 1
            if (array[m] <= x) {
                //array[m] <= x && abc && ** && r - l > 1 && l < m < r && array[r] <= x (comm: r - l > 1 && m == l + r / 2 => l < m < r)
                //=> l < Res <= m (abc) && m - l < r - l (интервал поиска усменьшился)
                // ** && abc && m - l < r - l => предусловие для функции, поэтому имеем право вызвать BinarySearchRec
                //и она обязана выполнить конракт и тогда наша функция вернет Res
                //(причем интервал поиска уменьшился => что рекурсия закончится)
                //pred: array[m] <= x && array != null && array[i] >= array[j] для любого i <= j && -1 <= l && r <= array.length && abc
                return recursiveLeftBinarySearch(array, x, l, m);
                //post: совпадает с постусловием для функции
            } else {
                //array[m] > x && abc && ** && r - l > 1 ** l < m < r && array[l] > x (comm: r - l > 1 && m == l + r / 2 => l < m < r)
                //=> m < Res <= r (abc) && r - m < r - l (интервал поиска усменьшился)
                // ** && abc && r - m < r - l => предусловие для функции, поэтому имеем право вызвать BinarySearchRec
                //и она обязана выполнить конракт и тогда наша функция вернет Res
                //(причем интервал поиска уменьшился => что рекурсия закончится)
                //pred: array[m] > x && array != null && array[i] >= array[j] для любого i <= j && -1 <= l && r <= array.length && abc
                return recursiveLeftBinarySearch(array, x, m, r);
                //post: совпадает с постусловием для функции
            }
            //post: просто постусловие функции
        }
        //post: просто постусловие функции
    }

    //pred: array != null && array[i] >= array[j] для любого i <= j
    //назову это условие ** (чтобы не писать его целиком дальше)
    //post: Res : (array[Res] >= x && (array[Res + 1] < x || Res + 1 == array.length))
    //|| (Res == -1 && array[0] < x)
    public static int iterativeRightBinarySearch(final int[] array, final int x) {
        // **
        //pred: true
        int l = -1;
        //post: ** && l == -1
        //pred: true
        int r = array.length;
        //post: ** && l == -1 && r == array.length

        //inv: -1 <= l <= Res < r <= length
        //pred: r - l > 1
        //post: inv && ** && r - l <= 1 && l < r
        //** && l == -1 && r == array.length => inv
        while (r > 1 + l) {
            //inv && r - l > 1 && **
            //pred: r - l > 1
            int m = l + (r - l) / 2;
            //post: m == (l + r) / 2
            //inv && r - l > 1 && ** && m == (l + r) / 2 => l < m < r

            //pred: r - l > 1
            if (array[m] < x) {
                // (array[m] < x && **) -> l <= Res < m && l < m < r
                //pred: array[m] < x
                r = m;
                //post: r == m
                //((array[m] < x && **) -> l <= Res < m && r == m )
                //=> inv && r - l -- уменьшилось (хотя бы на 1) тк r уменьшилось
            } else {
                // (array[m] >= x && **) -> m <= Res < r && l < m < r
                //pred: array[m] > x
                l = m;
                //post: l == m
                //((array[m] >= x && **) -> l <= Res < r` && l == m)
                //=> inv && r - l -- уменьшилось тк число которое вычитаем увеличилось (хотя бы на 1)
            }
            //post: inv && r - l < r' - l'
        }
        // r > l (из инварианта) && r - l <= 1 => r - l == 1
        // l <= Res < r && r - l == 1 => Res == l
        return l;//выполнили контракт
    }

    //pred: args != null && для всех 0 <= i < args.length : (args[i] != null && args[i] - целое число) && args.length >= 1
    // 1 <= i <= j < args.length => args[i] >= args[j] назову это условие ~~ (чтобы не писать целиком)
    //post: prints left and length to System.out where :
    //let a[0, ..., n] - is args[1, ..., args.length - 1].toint; x = args[0].toint
    //let a[-1] = +inf, a[a.length] = -inf
    // left : min{-1 < i <= a.length : x >= a[i]}
    // right : max{-1 <= i < a.length : a[i] >= x}
    // length = right - left + 1
    public static void main(final String[] args) {
        //~~
        //pred: true
        int x = Integer.parseInt(args[0]);
        //post: x == (int) args[0]
        // ~~ && x == (int) args[0]
        //pred: true
        int[] array = new int[args.length - 1];
        //post: array is int[] && array.size == args.length - 1

        //~~ &&  x == (int) args[0] && array.size == args.length - 1

        //pred: true
        int i = 0;
        //post: i == 0
        //~~ &&  x == (int) args[0] && array is int[] && array.size == args.length - 1 && i == 0 => inv
        //pred: true
        //inv: для всех j < i => array[j] == (int) args[j + 1]
        while (i < args.length - 1) {
            //inv && i < args.length && ~~
            //pred: i < args.length
            array[i] = Integer.parseInt(args[i + 1]);
            //post: array[i] == (int) args[i + 1]
            //inv' && i < args.length && array[i] == (int) args[i + 1] && ~~
            //pred: i < args.length
            i++;
            //post: i = i` + 1
            //inv' && i < args.length && array[i] == (int) args[i + 1] && ~~ && i == i' + 1 => inv
        }
        //post: ~~ && inv

        //видно, что предусловия для функций выполнены
        //pred: 0 <= i < array.length => array[i] != null && array[i] >= array[j] для любого i <= j
        int left = recursiveLeftBinarySearch(array, x, -1, array.length);
        //post: left : (array[left] <= x && (array[left - 1] > x || left == 0))
        //|| (left == array.length && array[array.length - 1] > x) назову это условие **

        //pred: 0 <= i < array.length => array[i] != null && array[i] >= array[j] для любого i <= j
        int right = iterativeRightBinarySearch(array, x);
        //post: right : (array[right] >= x && (array[right + 1] < x || right + 1 == array.length))
        //|| (right == -1 && array[0] < x) назову это условие $$

        //pred: true
        int length = right - left + 1;
        //post: length == right - left + 1
        // |[l, r] intersec with Z| == r - l + 1
        //** && $$ => post
        System.out.println(left + " " + length);
        //post: то же, что и функции main
    }
}
