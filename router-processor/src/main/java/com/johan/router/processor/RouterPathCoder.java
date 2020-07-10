package com.johan.router.processor;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

/**
 * Created by johan on 2020/7/6.
 */

public class RouterPathCoder {

    private ProcessingEnvironment processingEnvironment;

    public RouterPathCoder(ProcessingEnvironment processingEnvironment) {
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
            builder.append("\t").append("public static final String ")
                    .append(element.getSimpleName()).append("Path = ")
                    .append("\"").append(RouterHelper.buildPath(processingEnvironment, element)).append("\"")
                    .append(";").append("\n");
        }
        builder.append("\n");
        // 结束
        builder.append("}");
        return builder.toString();
    }

}
