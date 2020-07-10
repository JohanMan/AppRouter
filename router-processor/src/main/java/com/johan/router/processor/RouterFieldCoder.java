package com.johan.router.processor;

import java.lang.reflect.Field;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.VariableElement;

/**
 * Created by johan on 2020/7/6.
 */

public class RouterFieldCoder {

    private ProcessingEnvironment processingEnvironment;

    public RouterFieldCoder(ProcessingEnvironment processingEnvironment) {
        this.processingEnvironment = processingEnvironment;
    }

    public String create(String packageName, String className, Set<? extends Element> elements) {
        if (elements == null || elements.size() == 0) {
            throw new IllegalArgumentException("elements must > 0 !!");
        }
        StringBuilder builder = new StringBuilder();
        // 包名
        builder.append("package ").append(packageName).append(";").append("\n\n");
        // 导入包
        // 开始
        builder.append("public class ").append(className).append(" {").append("\n\n");
        // 字段
        for (Element element : elements) {
            String simpleName = element.getSimpleName().toString();
            String keyName = simpleName.substring(0, 1).toUpperCase() + simpleName.substring(1);
            String key = element.getEnclosingElement().getSimpleName() + "_" + keyName;
            String value = element.getSimpleName().toString();
            builder.append("\t").append("public static final String ")
                    .append(key).append(" = ")
                    .append("\"").append(value).append("\"")
                    .append(";").append("\n");
        }
        builder.append("\n");
        // 结束
        builder.append("}");
        return builder.toString();
    }

}
