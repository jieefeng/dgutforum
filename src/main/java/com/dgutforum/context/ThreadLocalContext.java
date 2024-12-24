package com.dgutforum.context;

public class ThreadLocalContext {

    // 创建一个 ThreadLocal 变量，用于存储当前线程的 ID 信息
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    // 设置线程局部变量
    public static void setUserId(Long userId) {
        threadLocal.set(userId);
    }

    // 获取线程局部变量
    public static Long getUserId() {
        return threadLocal.get();
    }

    // 清除线程局部变量
    public static void remove() {
        threadLocal.remove();
    }
}
