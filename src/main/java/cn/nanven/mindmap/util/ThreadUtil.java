package cn.nanven.mindmap.util;

public class ThreadUtil {
    public static void run(Runnable task) {
        Thread thread = new Thread(task);
        thread.start();
    }
}
