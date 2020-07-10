package com.johan.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by johan on 2020/7/8.
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface AutoField {
}
