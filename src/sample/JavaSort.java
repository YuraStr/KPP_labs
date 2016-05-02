package sample;

/**
 * Created by yura_str on 5/2/16.
 */
public class JavaSort {
  void sort(int[] xs) {
    sort(xs, 0, xs.length - 1);
  }

  void sort(int[] xs, int l, int r) {
    int pivot = xs[(l + r) / 2];
    int a = l;
    int b = r;
    while (a <= b) {
      while (xs[a] < pivot) {
        ++a;
      }
      while (xs[b] > pivot) {
        --b;
      }
      if (a <= b) {
        swap(xs, a, b);
        ++a;
        --b;
      }
    }
    if (l < b) sort(xs, l, b);
    if (b < r) sort(xs, a, r);
  }

  void swap(int[] arr, int i, int j) {
    int t = arr[i];
    arr[i] = arr[j];
    arr[j] = t;
  }
}
