package org.example.mvc;

import org.example.mvc.annotation.Controller;
import org.example.mvc.annotation.RequestMapping;
import org.example.mvc.controller.RequestMethod;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {


    private final Object[] basePackage;

    private Map<HandlerKey, AnnotationHandler> handlers = new HashMap< >();

    public AnnotationHandlerMapping(Object... basePackages) {
        this.basePackage = basePackages;
    }

    @Override
    public Object findHandler(HandlerKey handlerKey) {
        return handlers.get(handlerKey);
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        // @Controller가 있는 HomeController 가 넘어옴
        Set<Class<?>> clazzestypesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);

        clazzestypesAnnotatedWith.forEach(clazz -> {
            Arrays.stream(clazz.getDeclaredMethods()).forEach(declaredMethod -> {
                RequestMapping requestMapping = declaredMethod.getDeclaredAnnotation(RequestMapping.class);

                Arrays.stream(getRequestMethods(requestMapping))
                        .forEach(requestMethod -> handlers.put(
                                new HandlerKey(requestMethod, requestMapping.value()), new AnnotationHandler(clazz, declaredMethod)
                        ));
            });
        });
    }

    private RequestMethod[] getRequestMethods(RequestMapping requestMapping) {
        return requestMapping.method();
    }
}
