package search;

public class BinarySearch {
    // Less(a, r, x) = for i = 0..r - 1 a[i] > x && (r == a.length || a[r] <= x)
    // Decreasing(a) = for i = 0..a.length - 2 a[i] <= a[i + 1]

    // Invar(a, l, r) = -1 <= l < r <= arr.length
    //      && (l == -1 || arr[l] > x)
    //      && (r == arr.length || arr[r] <= x)
    //      && Decreasing(arr)

    // Pred: Invar(arr, l, r)

    // Post: Less(arr, R, x)
    static int recBS(int[] arr, int x, int l, int r) {
        // Invar(arr, l, r)
        if (r - l == 1) {
            // (0)
            // Invar(arr, l, r)
            // && r == l + 1
            // =>
            // Less(arr, r, x)

            // a) l == -1
            //    r == 0 && arr[r] <= x =>
            // => arr[r] <= x
            //    && any 0 <= i < R arr[i] > x
            // => Less(arr, r, x)

            // b) l > -1
            //    (r == arr.length || arr[r] <= x) && r == l + 1 && arr[l] > x && Decreasing(arr) => (mathematical induction)
            // => (r == arr.length || arr[r] <= x) && r == l + 1 && for i = 0..l arr[l] > x =>
            // => (r == arr.length || arr[r] <= x) && any 0 <= i < r arr[i] > x
            // => Less(arr, r, x)

            return r;
        }
        // Invar(arr, l, r)
        // && r - l > 1
        int m = (l + r) / 2;
        // (1)
        // r - l > 1
        // =>
        // l < (l + r) / 2 < r

        // r - l > 1
        // r + l - 1 > 2 * l
        // (r + l) / 2 >= (r + l - 1) / 2 > l
        // m > l

        // r - l > 1
        // 2 * r > 1 + l + r > l + r
        // r > (l + r) / 2
        // r > m

        // Invar(arr, l, r)
        // && l < m < r
        if (arr[m] > x) {
            // Invar(arr, l, r)
            // && l < m < r
            // && arr[m] > x
            l = m;
            // 0 <= l < r <= arr.length
            // && arr[l] > x
            // && (r == arr.length || arr[r] <= x)
            // && Decreasing(arr)
        } else {
            // Invar(arr, l, r)
            // && l < m < r
            // && arr[m] <= x
            r = m;
            // -1 <= l < r < arr.length
            // && (l == -1 || arr[l] > x)
            // && arr[r] <= x
            // && Decreasing(arr)
        }
        // Invar(arr, l, r)
        return recBS(arr, x, l, r);
        // (R == arr.length || arr[R] <= x)
        // && any 0 <= i < R arr[i] > x
    }

    //Pred: Decreasing(arr)
    //Post: Less(arr, R, x)
    static int iterBS(int[] arr, int x) {
        // Decreasing(arr)
        int l = -1;
        int r = arr.length;
        // Invar(arr, l, r)
        while (l + 1 < r) {
            // Invar(arr, l, r)
            // && l + 1 < r
            int m = (l + r) / 2;
            // l + 1 < r
            // (1) =>
            // l < m < r

            // Invar(arr, l, r)
            // && l < m < r
            if (arr[m] <= x) {
                // Invar(arr, l, r)
                // && l < m < r
                // && arr[m] <= x
                r = m;
                // Decreasing(arr)
                // && -1 <= l < r < arr.length
                // && (l == -1 || arr[l] > x)
                // && arr[r] <= x
            } else {
                // Invar(arr, l, r)
                // && l < m < r
                // && arr[m] > x
                l = m;
                // Decreasing(arr)
                // && 0 <= l < r <= arr.length
                // && arr[l] > x
                // && (r == arr.length || arr[r] <= x)
            }
            // Invar(arr, l, r)
        }
        // Invar(arr, l, r)
        // && l + 1 = r
        // (0) =>
        // Less(arr, r, x)
        return r;
    }

    //Pred: Decreasing(arr)
    //Post: Less(arr, rec, x)
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int n = args.length;
        int[] arr = new int[n - 1];
        for (int i = 0; i < n - 1; i++) {
            arr[i] = Integer.parseInt(args[i + 1]);
        }

        // Decreasing(arr)
        int iter = iterBS(arr, x);
        // Less(arr, iter, x)

        //Invar(arr, -1, arr.length)
        int rec = recBS(arr, x, -1, arr.length);
        // Less(arr, rec, x)

        if (iter == rec) {
            System.out.println(iterBS(arr, x));
        }
    }
}
