package com.wuzz.demo.orm.core.common.jdbc.datasource;


import org.aspectj.lang.JoinPoint;

/**
 * @description:动态切换数据源
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/25 15:55
 * @since 1.0
 **/
public class DynamicDataSourceEntry {

    // 默认数据源
    public final static String DEFAULT_SOURCE = null;

    private final static ThreadLocal<String> local = new ThreadLocal<String>();

    /**
     * 清空数据源
     */
    public void clear() {
        local.remove();
    }

    /**
     * 获取当前正在使用的数据源名字
     *
     * @return String
     */
    public String get() {
        String s = local.get();
        System.out.println("当前数据源：" + s);
        return local.get();
    }

    /**
     * 还原指定切面的数据源
     *
     * @param join
     */
    public void restore(JoinPoint join) {
        local.set(DEFAULT_SOURCE);
    }

    /**
     * 还原当前切面的数据源
     */
    public void restore() {
        local.set(DEFAULT_SOURCE);
    }

    /**
     * 设置已知名字的数据源
     *
     * @param source
     */
    public void set(String source) {
        local.set(source);
    }

    /**
     * 根据年份动态设置数据源
     *
     * @param year
     */
    public void set(int year) {
        local.set("DB_ORM" + year);
    }
}
