package com.wuzz.demo.orm.framework;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

/**
 * @description:
 * @author: Wuzhenzhao@hikvision.com.cn
 * @time 2020/3/25 15:58
 * @since 1.0
 **/
public class ClassMappings {

    private ClassMappings() {
    }

    static final Set<Class<?>> SUPPORTED_SQL_OBJECTS = new HashSet<Class<?>>();

    static {
        //只要这里写了的，默认支持自动类型转换
        Class<?>[] classes = {
                boolean.class, Boolean.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                float.class, Float.class,
                double.class, Double.class,
                String.class,
                Date.class,
                Timestamp.class,
                BigDecimal.class
        };
        SUPPORTED_SQL_OBJECTS.addAll(Arrays.asList(classes));
    }

    //是否支持的sql类型
    static boolean isSupportedSQLObject(Class<?> clazz) {
        return clazz.isEnum() || SUPPORTED_SQL_OBJECTS.contains(clazz);
    }

    //获取get
    public static Map<String, Method> findPublicGetters(Class<?> clazz) {
        Map<String, Method> map = new HashMap<String, Method>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()))
                continue;
            if (method.getParameterTypes().length != 0)
                continue;
            if (method.getName().equals("getClass"))
                continue;
            Class<?> returnType = method.getReturnType();
            if (void.class.equals(returnType))
                continue;
            if (!isSupportedSQLObject(returnType)) {
                continue;
            }
            if ((returnType.equals(boolean.class)
                    || returnType.equals(Boolean.class))
                    && method.getName().startsWith("is")
                    && method.getName().length() > 2) {
                map.put(getGetterName(method), method);
                continue;
            }
            if (!method.getName().startsWith("get"))
                continue;
            if (method.getName().length() < 4)
                continue;
            map.put(getGetterName(method), method);
        }
        return map;
    }

    //获取属性
    public static Field[] findFields(Class<?> clazz) {
        return clazz.getDeclaredFields();
    }

    //属性的set方法
    public static Map<String, Method> findPublicSetters(Class<?> clazz) {
        Map<String, Method> map = new HashMap<String, Method>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()))
                continue;
            if (!void.class.equals(method.getReturnType()))
                continue;
            if (method.getParameterTypes().length != 1)
                continue;
            if (!method.getName().startsWith("set"))
                continue;
            if (method.getName().length() < 4)
                continue;
            if (!isSupportedSQLObject(method.getParameterTypes()[0])) {
                continue;
            }
            map.put(getSetterName(method), method);
        }
        return map;
    }

    //获取get方法名称
    public static String getGetterName(Method getter) {
        String name = getter.getName();
        if (name.startsWith("is"))
            name = name.substring(2);
        else
            name = name.substring(3);
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    //获取set方法名称
    private static String getSetterName(Method setter) {
        String name = setter.getName().substring(3);
        return Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }
}
