package com.johan.router.processor;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by johan on 2020/7/7.
 */

public class RouterHelper {

    /**
     * 获取所有 Element 公共包名
     * @param processingEnvironment
     * @param elements
     * @return
     */
    public static String getPackageName(ProcessingEnvironment processingEnvironment, Set<? extends Element> elements) {
        if (elements.size() == 0) return null;
        String[] packages = new String[8];
        Iterator<? extends Element> iterator = elements.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            Element element = iterator.next();
            String elementPackageName = getElementPackageName(processingEnvironment, element);
            String[] currents = elementPackageName.split("\\.");
            int size = Math.min(packages.length, currents.length);
            if (index == 0) {
                for (int i = 0; i < size; i++) {
                    packages[i] = currents[i];
                }
            } else {
                for (int i = 0; i < size; i++) {
                    if (packages[i] == null) continue;
                    if (packages[i].equalsIgnoreCase(currents[i])) continue;
                    packages[i] = null;
                }
            }
            index ++;
        }
        StringBuilder builder = new StringBuilder();
        for (String packageName : packages) {
            if (packageName == null) break;
            if (builder.length() == 0) {
                builder.append(packageName);
            } else {
                builder.append(".").append(packageName);
            }
        }
        return builder.toString();
    }


    /**
     * 获取 Element 类名
     * @param element
     * @return
     */
    public static String getElementClassName(Element element) {
        return element.getSimpleName().toString();
    }

    /**
     * 获取 Element 包名
     * @param processingEnvironment
     * @param element
     * @return
     */
    public static String getElementPackageName(ProcessingEnvironment processingEnvironment, Element element) {
        return processingEnvironment.getElementUtils().getPackageOf(element.getEnclosingElement()).asType().toString();
    }

    /**
     * 获取路径
     * @param processingEnvironment
     * @param element
     * @return
     */
    public static String buildPath(ProcessingEnvironment processingEnvironment, Element element) {
        String elementPackageName = RouterHelper.getElementPackageName(processingEnvironment, element);
        String elementClassName = RouterHelper.getElementClassName(element);
        return elementPackageName.replaceAll("\\.", "/") + "/" + elementClassName;
    }

    public static String buildClass(ProcessingEnvironment processingEnvironment, Element element) {
        String elementPackageName = RouterHelper.getElementPackageName(processingEnvironment, element);
        String elementClassName = RouterHelper.getElementClassName(element);
        return elementPackageName + "." + elementClassName + ".class";
    }

    /**
     * 生成Java源文件
     * @param processingEnvironment
     * @param packageName
     * @param className
     * @param javaCode
     */
    public static void writeJavaFile(ProcessingEnvironment processingEnvironment, String packageName, String className, String javaCode) {
        try {
            JavaFileObject source = processingEnvironment.getFiler()
                    .createSourceFile(packageName + "." + className);
            Writer writer = source.openWriter();
            writer.write(javaCode);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            log(processingEnvironment, "写入文件失败 : " + e.getLocalizedMessage());
        }
    }

    /**
     * 日志
     * @param processingEnvironment
     * @param info
     */
    public static void log(ProcessingEnvironment processingEnvironment, String info) {
        Messager messager = processingEnvironment.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "** Log ** \n" + info);
    }

}
