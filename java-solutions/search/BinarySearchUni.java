package search;

public class BinarySearchUni {
    // Decreasing(a, l, r) = for i = l..r - 2 a[i] > a[i + 1]       [l;r)
    // Increasing(a, l, r) = for i = l..r - 2 a[i] < a[i + 1]       [l;r)
    // Bot(a, m) =  1 <= m <= a.length
    //              && Decreasing(a, 0, m)
    //              && Increasing(a, m, a.length)
    // s(a): Bot(a, s(a)) && !Exist m < s(a) : Bot(a, m)

    // Pred: Exist m : Bot(arr, m)
    // Post: R = s(arr)
    static int iterUni(int[] arr) {
        // s = s(arr)
        int l = -1;
        int r = arr.length - 1;
        // -1 <= l < s <= r < arr.length
        while (r - l > 1) {
            // (0)
            //      -1 <= l < s <= r < arr.length
            //      && l + 1 < r
            // =>
            //      l < (r + l) / 2 < r

            // l + 1 < r
            // 2 * l < r + l - 1
            // l < (r + l - 1) / 2 <= (r + l) / 2 = m

            // l + 1 < r
            // l + r + 1 < 2 * r
            // r > (l + r + 1) / 2 >= (r + l) / 2 = m

            // l < (r + l) / 2 < r
            int m = (r + l) / 2;
            // -1 <= l < s <= r < arr.length
            // && l < m < r
            if (arr[m] < arr[m + 1]) {
                // (1)
                //      -1 <= l < s <= r < arr.length
                //      && -1 <= l < m < r < arr.length
                //      && arr[m] < arr[m + 1]
                // =>
                //      m >= s

                // a) m < s - 1
                //    m < s - 1 && arr[m] < arr[m + 1] && Bot(arr, s) =>
                // => m < s - 1 && arr[m] < arr[m + 1] && Decreasing(arr, 0, s) =>
                // => m <= s - 2 && arr[m] < arr[m + 1] && for i = 0..s - 2 a[i] > a[i + 1] =>
                // => arr[m] < arr[m + 1] && arr[m] > arr[m + 1] !!!

                // b) m == s - 1
                //    arr[s - 1] < arr[s] && Bot(arr, s) =>
                // => Decreasing(arr, 0, s) && Increasing(arr, s, arr.length)
                //    && arr[s - 1] < arr[s] =>
                // => Decreasing(arr, 0, s - 1) && Increasing(arr, s - 1, arr.length) =>
                // => Decreasing(arr, 0, m) && Increasing(arr, m, arr.length) =>
                // => Bot(arr, m) && m < s !!!
                r = m;
                // -1 <= l < s <= r' < r < arr.length
            } else {
                // (2)
                //      -1 <= l < s <= r < arr.length
                //      && -1 <= l < m < r < arr.length
                //      && arr[m] >= arr[m + 1]
                // =>
                //      m < s

                // Bot(arr, s) && arr[m] >= arr[m + 1] =>
                // Increasing(s, arr.length) && arr[m] >= arr[m + 1] =>
                // => for i = s..arr.length - 2 a[i] < a[i + 1] && arr[m] >= arr[m + 1]
                // => m < s
                l = m;
                // -1 <= l < l' < s <= r < arr.length
            }
        }
        // -1 <= l < s <= r < arr.length
        // && l + 1 >= r

        // l + 1 >= r
        // l < s <= r
        // -> r = s
        return r;
    }

    //Pred: Exist m : Bot(arr, m) && -1 <= l < s <= r < arr.length
    //Post: R = s(arr)
    static int recUni(int[] arr, int l, int r) {
        //s = s(arr)
        if (l + 1 >= r) {
            // l + 1 >= r
            // -1 <= l < s <= r < arr.length
            // -> r = s
            return r;
        }
        //      -1 <= l < s <= r < arr.length
        //      && l + 1 < r
        // (0) =>
        //      l < (r + l) / 2 < r

        int m = (l + r) / 2;
        // -1 <= l < s <= r < arr.length
        // && l < m < r
        if (arr[m] < arr[m + 1]) {
            //      -1 <= l < s <= r < arr.length
            //      && -1 <= l < m < r < arr.length
            //      && arr[m] < arr[m + 1]
            // (1) =>
            //      m >= s

            // -1 <= l < s <= m < arr.length
            // && Exist m : Bot(arr, m)
            return recUni(arr, l, m);
            // R = s(arr)
        } else {
            //      -1 <= l < s <= r < arr.length
            //      && -1 <= l < m < r < arr.length
            //      && arr[m] >= arr[m + 1]
            // (2) =>
            //      m < s

            // -1 <= m < s <= r < arr.length
            // && Exist m : Bot(arr, m)
            return recUni(arr, m, r);
            // R = s(arr)
        }
    }

    // Pred: Exist m : Bot(arr, m)
    // Post: R = s(arr)
    public static void main(String[] args) {
        int n = args.length;
        int[] arr = new int[n];
        for (int i = 0; i < args.length; i++) {
            arr[i] = Integer.parseInt(args[i]);
        }

        // Exist m : Bot(arr, m)
        int iter = iterUni(arr);
        // iter = s(arr)

        // Exist m : Bot(arr, m) && -1 <= l < s <= r < arr.length
        int rec = recUni(arr, -1, arr.length - 1);
        // rec = s(arr)

        if (iter == rec) {
            System.out.println(iter);
        }
    }
}
