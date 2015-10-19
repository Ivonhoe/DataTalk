package util;

public class Log {

    static boolean DEBUG = true;
    static boolean WARNING = false;
    static boolean ERROR = false;

    public static void d(String str) {
        if (DEBUG)
            System.out.println(str);
    }

    public static void w(String str) {
        if (WARNING)
            System.out.println(str);
    }

    public static void e(String str) {
        if (ERROR)
            System.out.println(str);
    }
}
