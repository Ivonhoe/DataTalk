package util.sort;

/**
 * @author ivonhoe on 15-6-7.
 */
public class InsertSort {

    /*
    * 直接插入排序
    * */
    public static void directInsert(ElemType[] array) {
        int n = array.length, j;
        ElemType temp;
        for (int i = 1; i < n; i++) {
            if (array[i].getValue() < array[i - 1].getValue()) {
                temp = array[i];
                for (j = i - 1; j >= 0 && temp.getValue() < array[j].getValue() ; j--) {
                    array[j + 1] = array[j];
                }
                array[j + 1] = temp;
            }
        }
    }

    /**
     * 折半插入排序
     */
    public static void binaryInsert(ElemType[] array) {
        int n = array.length, j, low, high, mid;

        ElemType temp;
        for (int i = 1; i < n; i++) {
            temp = array[i];
            low = 0;
            high = i - 1;

            // 折半查找
            while (low <= high) {
                mid = (low + high) / 2;
                if (array[mid].getValue() > temp.getValue()) {
                    high = mid - 1;
                }
            }

            // 整体移动
            for (j = i - 1; j >= high + 1; j--) {
                array[j + 1] = array[j];
            }

            // 插入操作
            array[high + 1] = temp;
        }
    }

    /**
     * 希尔排序,
     */
    public static void shellSort(ElemType[] array) {
        int increase = array.length, j;

        ElemType temp;

        while (increase > 0) {
            increase = increase / 2;
            for (int i = increase; i < array.length; i++) {
                temp = array[i];
                for (j = i - increase;
                     j >= 0 && temp.getValue() < array[j].getValue(); j -= increase) {
                    array[j + increase] = array[j];
                }
                array[j + increase] = temp;
            }
        }
    }

    public static void shellSort(ElemType[] array, int n) {

    }
}
