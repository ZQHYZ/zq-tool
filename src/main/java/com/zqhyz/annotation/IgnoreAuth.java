package com.zqhyz.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface IgnoreAuth {
    String value() default "";
}
