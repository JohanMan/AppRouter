package com.johan.router.processor;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;

/**
 * Created by johan on 2020/7/6.
 */

public class RouterBuilderCoder {

    private ProcessingEnvironment processingEnvironment;

    public RouterBuilderCoder(ProcessingEnvironment processingEnvironment) {
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
        builder.append("import com.johan.router.AppRouter;").append("\n");
        builder.append("import com.johan.router.RouterBuilder;").append("\n\n");
        // 开始
        builder.append("public class ").append(className).append(" implements RouterBuilder {").append("\n\n");
        // createMap 方法
        builder.append("\t").append("@Override").append("\n");
        builder.append("\t").append("public void build(AppRouter router) {").append("\n");
        for (Element element : elements) {
            String key = RouterHelper.buildPath(processingEnvironment, element);
            String value = RouterHelper.buildClass(processingEnvironment, element);
            builder.append("\t\t").append("router.putRoute(\"").append(key).append("\", ").append(value).append(");").append("\n");
        }
        builder.append("\t").append("}").append("\n\n");
        // 结束
        builder.append("}");
        return builder.toString();
    }

}
