package search;

public class BinarySearch {
    //pred: array != null && array[i] >= array[j] для любого i <= j && array[array.length - 1] <= x
    //назову это условие ** (чтобы не писать его целиком дальше)
    //post: (array[Res] <= x && (array[Res - 1] > x || Res == 0))
    //inv: l < Res <= r && r' - l' < r - l
    public static int RecursiveLeftBinarySearch(final int[] array, int x, int l, int r) {
        //inv && **
        if (r - l <= 1) {
            // r > l (из инварианта) && r - l <= 1 => r - l == 1
            // r - l == 1 && inv && ** => Res == r
            return r; // выполнили контракт
        } else {
            // inv && ** && r - l > 1
            //pred: r - l > 1
            int m = l + (r - l) / 2;
            //post: l + (r - l) / 2
            //inv && ** && r - l > 1 && m == l + (r - l) / 2 => l < m < r
            if (array[m] <= x) {
                //array[m] <= x && inv && ** && r - l > 1 && l < m < r && array[r] <= x (comm: r - l > 1 && m == l + r / 2 => l < m < r)
                //=> l < Res <= m (inv) && m - l < r - l (интервал поиска усменьшился)
                // ** && inv && m - l < r - l => предусловие для функции, поэтому имеем право вызвать BinarySearchRec
                //и она обязана выполнить конракт и тогда наша функция вернет Res
                //(причем интервал поиска уменьшился => что рекурсия закончится)
                return RecursiveLeftBinarySearch(array, x, l, m);
            } else {
                //array[m] > x && inv && ** && r - l > 1 ** l < m < r && array[l] > x (comm: r - l > 1 && m == l + r / 2 => l < m < r)
                //=> m < Res <= r (inv) && r - m < r - l (интервал поиска усменьшился)
                // ** && inv && r - m < r - l => предусловие для функции, поэтому имеем право вызвать BinarySearchRec
                //и она обязана выполнить конракт и тогда наша функция вернет Res
                //(причем интервал поиска уменьшился => что рекурсия закончится)
                return RecursiveLeftBinarySearch(array, x, m, r);
            }
            //из блока if/else не выйти.
        }
    }

    //pred: array != null && array[i] >= array[j] для любого i <= j && array[array.length - 1] <= x
    //назову это условие ** (чтобы не писать его целиком дальше)
    //post:  array[Res] >= x && (array[Res + 1] < x || Res == array.length - 1)
    public static int IterativeRightBinarySearch(final int[] array, final int x) {
        // **
        //pred: true
        int l = -1;
        //post: ** && l == -1
        //pred: true
        int r = array.length ;
        //post: ** && l == -1 && r == array.length

        //inv: l <= Res < r
        //pred: r - l > 1
        //post: inv && ** && r - l <= 1 && l < r
        //** && l == -1 && r == array.length => inv
        while (r - l > 1) {
            //inv && r - l > 1 && **
            //pred: r - l > 1
            int m = l + (r - l) / 2;
            //post: m == (l + (r - l) / 2
            //inv && r - l > 1 && ** && m == l + (r - l) / 2 => l < m < r

            //pred: r - l > 1
            if (array[m] < x) {
                // (array[m] < x && **) -> l <= Res < m && l < m < r
                //pred: array[m] < x
                r = m;
                //post: r == m
                //((array[m] < x && **) -> l <= Res < m && r == m ) && array[l] (т.к r == m) <= x
                //=> inv && r - l -- уменьшилось (хотя бы на 1) тк r уменьшилось
            } else {
                // (array[m] >= x && **) -> m <= Res < r && l < m < r
                //pred: array[m] > x
                l = m;
                //post: l == m
                //((array[m] >= x && **) -> l <= Res < r` && l == m) && array[l] (т.к l == m) > x
                //=> inv && r - l -- уменьшилось тк число которое вычитаем увеличилось (хотя бы на 1)
            }
            //post: inv && r - l -- уменьшилось(хотя бы на 1)
        }
        // r > l (из инварианта) && r - l <= 1 => r - l == 1
        // l <= Res < r && r - l == 1 && array[l] > x && array[r] >= x => Res == r
        //pred: true
        //post: result == l == Res
        return l;//выполнили контракт
    }


    //pred: array != null && array[i] >= array[j] для любого i <= j && array[array.length - 1] <= x
    //post: ответ на задачу
    public static void BinarySearchSpan(int[] array, int x) {
        int left = RecursiveLeftBinarySearch(array, x, -1, array.length);
        //array[left] >= x && (array[left + 1] < x || left == array.length - 1)
        int length = IterativeRightBinarySearch(array, x) - left;
        // length == r - l - 1
        System.out.println(left + " " + (length + 1));
    }

    //pred: args != null && args.length >= 1
    //post: array[Res] <= x && (array[Res - 1] > x || Res == 0) или Res == array.length, если array[array.length - 1] > x
    public static void main(String[] args) {
        //args != null && args.length >= 1
        //pred: true
        int x = Integer.parseInt(args[0]);
        //post: x == (int) args[0]
        // args.length >= 1 && x == (int) args[0]
        //pred: true
        int[] array = new int[args.length - 1];
        //post: array.size == args.lengt - 1
        //args.length >= 1 &&  x == (int) args[0] && array.size == args.length - 1

        //pred: true
        //inv: для всех j < i => array[j] == (int) args[j + 1]
        //post: inv
        for (int i = 0; i < args.length - 1; i++) {
            //pred: i < args.length - 1
            array[i] = Integer.parseInt(args[i + 1]);
            //post: array[i] == (int) args[i + 1]
        }
        //array -- содержит массив из условия && x -- число из условия
        if (array.length == 0) {
            //array == null && array -- массив из условия
            System.out.println(0 + " " + 0);
            //выполнили контракт(т.к ответ 0)
            return;
        }
        //array != null && array -- массив из условия && x -- число из условия
        //array != null && array -- массив из условия && x -- число из условия && array[array.length - 1] <= x
        //предусловие функции выполнено
        //pred: true
        BinarySearchSpan(array, x);
    }
}
