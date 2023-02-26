package queue;

// Model:
//       a[1]..a[n],  for i = 1..n a[i] != null, n >= 0

// immutable(a, n) = for i = 1..n a'[i] = a[i]
// predicate(a[i]) = predicate.test(a[i])
// count(a, predicate) = | {i in [1..n] : predicate(a[i])} |

import java.util.function.Predicate;

public interface Queue {
    // enqueue:
    //       Pred: element != null
    //       Post: n' = n + 1 && a[n'] = element && immutable(a, n)
    void enqueue(Object element);

    // element:
    //       Pred: n >= 1
    //       Post: R = a[1] && for immutable(a, n)
    Object element();

    // dequeue:
    //       Pred: n >= 1
    //       Post: n' = n - 1 && for i = 1..n' a'[i] = a[i + 1] && R = a[1]
    Object dequeue();

    // size:
    //       Pred: true
    //       Post: R = n && immutable(a, n)
    int size();

    // isEmpty:
    //       Pred: true
    //       Post: R = (n == 0) && immutable(a, n)
    boolean isEmpty();

    // clear:
    //       Pred: true
    //       Post: n = 0
    void clear();

    // removeIf:
    //       Pred: predicate != null
    //       Post: n' = count(a, !predicate) && for i = 1..n predicate(a[i]) ^ a'[count(a[1..i], !predicate)] == a[i]
    void removeIf(Predicate<Object> predicate);

    // retainIf:
    //       Pred: predicate != null
    //       Post: n' = count(a, predicate) && for i = 1..n !predicate(a[i]) ^ a'[count(a[1..i], predicate)] == a[i]
    void retainIf(Predicate<Object> predicate);

    // takeWhile:
    //       Pred: predicate != null
    //       Post: count(a[1..n'], predicate) == n'
    //       && (n' == n || count(a[1..n' + 1], predicate) != n' + 1) && immutable(a, n')
    void takeWhile(Predicate<Object> predicate);

    // dropWhile:
    //       Pred: predicate != null
    //       Post: count(a[1..n - n'], !predicate) == n - n'
    //       && (n' == 0 || count(a[1..n - n' + 1], !predicate) != n - n' + 1) && for i = 1..n' a'[i] = a[n - n' + i]
    void dropWhile(Predicate<Object> predicate);
}
