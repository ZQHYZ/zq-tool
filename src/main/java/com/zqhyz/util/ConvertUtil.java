package com.zqhyz.util;

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.function.Consumer;

public class ConvertUtil {
    @SneakyThrows
    public static <V, T> V copyProperties(T source, Class<V> target, Consumer<V> consumer) {
        Constructor<V> constructor = target.getConstructor();
        V targetObj = constructor.newInstance();
        // 拷贝属性
        BeanUtils.copyProperties(source, targetObj);
        consumer.accept(targetObj);
        return targetObj;
    }

    @SneakyThrows
    public static <V> V dynamicConstructure(Class<V> target, Consumer<V> consumer) {
        Constructor<V> constructor = target.getConstructor();
        V targetObj = constructor.newInstance();
        consumer.accept(targetObj);
        return targetObj;
    }
}
