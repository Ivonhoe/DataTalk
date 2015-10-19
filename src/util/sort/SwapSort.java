package util.sort;

/**
 * @author ivonhoe on 15-6-7.
 */
public class SwapSort {

    /**
     * 冒泡排序
     */
    public static void bubbleSort(ElemType[] array) {
        int n = array.length;
        for (int i = 0; i < n; i++) {
            boolean flag = false;
            for (int j = n - 1; j > i; j--) {
                if (array[j].getValue() < array[j - 1].getValue()) {
                    // swap
                    swap(array, j, j - 1);
                    flag = true;
                }
            }
            if (!flag) {
                return;
            }
        }
    }

    /**
     * 快速排序，分治法，每次排序把排序表分成独立的两个部分
     */
    public static void QuickSort(ElemType[] array) {
        quickSort(array, 0, array.length - 1);
    }

    //快速排序算法
    private static void quickSort(ElemType[] nodes, int start, int end) {
        if (start >= end || start >= nodes.length || end >= nodes.length) {
            return;
        }
        //开始的第一个元素作为比较的基准值
        double weight = nodes[start].getValue();
        int i = start + 1;
        int j = end;
        while (true) {
            //i为前面的索引，向后搜索
            //j为最后的索引，向前搜索
            while (weight < nodes[j].getValue())
                j--;
            while (weight > nodes[i].getValue() && i < j)
                i++;
            if (i >= j)
                break;
            swap(nodes, i, j);
            if (nodes[i].getValue() == weight) {
                j--;
            } else {
                i++;
            }
        }
        swap(nodes, start, j);
        //递归排序
        if (start < i - 1) {
            quickSort(nodes, start, i - 1);
        }
        if (end > j + 1) {
            quickSort(nodes, j + 1, end);
        }

    }

    private static void swap(ElemType[] nodes, int i, int j) {
        ElemType temp = nodes[i];
        nodes[i] = nodes[j];
        nodes[j] = temp;
    }
}
