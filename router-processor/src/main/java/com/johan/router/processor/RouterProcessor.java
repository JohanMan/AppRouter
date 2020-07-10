package com.johan.router.processor;

import com.google.auto.service.AutoService;
import com.johan.router.annotation.AutoField;
import com.johan.router.annotation.AutoRouter;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes({"com.johan.router.annotation.AutoRouter", "com.johan.router.annotation.AutoField"})
public class RouterProcessor extends AbstractProcessor {

    private ProcessingEnvironment processingEnvironment;
    private String packageName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.processingEnvironment = processingEnvironment;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> autoRouterElements = roundEnvironment.getElementsAnnotatedWith(AutoRouter.class);
        if (autoRouterElements.size() == 0) return true;
        packageName = RouterHelper.getPackageName(processingEnvironment, autoRouterElements);
        processAutoRouterElement(autoRouterElements);
        Set<? extends Element> autoFieldElements = roundEnvironment.getElementsAnnotatedWith(AutoField.class);
        processAutoFieldElement(autoFieldElements);
        return true;
    }

    /**
     * 处理 AutoRouter 注解
     * @param elements
     */
    private void processAutoRouterElement(Set<? extends Element> elements) {
        if (elements.size() == 0) return;
        // AppRouterPath
        String routerPathClassName = "AppRouterPath";
        RouterPathCoder routerPathCoder = new RouterPathCoder(processingEnvironment);
        String routerPathJavaCode = routerPathCoder.create(packageName, routerPathClassName, elements);
        RouterHelper.writeJavaFile(processingEnvironment, packageName, routerPathClassName, routerPathJavaCode);
        // AppRouterBuilder
        String routerBuilderClassName = "AppRouterBuilder";
        RouterBuilderCoder routerBuilderCoder = new RouterBuilderCoder(processingEnvironment);
        String routerBuilderJavaCode = routerBuilderCoder.create(packageName, routerBuilderClassName, elements);
        RouterHelper.writeJavaFile(processingEnvironment, packageName, routerBuilderClassName, routerBuilderJavaCode);
    }

    /**
     * 处理 AutoField 注解
     * @param elements
     */
    private void processAutoFieldElement(Set<? extends Element> elements) {
        if (elements.size() == 0) return;
        // AppRouterField
        String routerFieldClassName = "AppRouterField";
        RouterFieldCoder routerFieldCoder = new RouterFieldCoder(processingEnvironment);
        String routerFieldJavaCode = routerFieldCoder.create(packageName, routerFieldClassName, elements);
        RouterHelper.writeJavaFile(processingEnvironment, packageName, routerFieldClassName, routerFieldJavaCode);
    }

}
