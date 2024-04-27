package org.example;

import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.example.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Controller 애노테이션이 설정돼 있는 모든 클래스를 찾아서 출력한다.
 */
public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void controllerScan() {
        Set<Class<?>> beans = getTypesAnnotatedWith(List.of(Controller.class, Service.class));

        logger.debug("beans: [{}]", beans);
    }

    @Test
    void showClass() {
        Class<User> clazz = User.class;
        logger.debug("class: [{}]", clazz.getName());

        logger.debug("User all declared fields: [{}]", Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList()));
        logger.debug("User all declared constructors: [{}]", Arrays.stream(clazz.getDeclaredConstructors()).collect(Collectors.toList()));
        logger.debug("User all declared methods: [{}]", Arrays.stream(clazz.getDeclaredMethods()).collect(Collectors.toList()));
    }

    /**
     * 힙 영역에 로드돼 있는 클래스 타입의 객체를 가져오는 방법
     */
    @Test
    void load() throws ClassNotFoundException {
        // 첫번째 방법
        Class<User> clazz = User.class;

        // 두번째 방법
        User user = new User("serverwizard", "홍홍홍");
        Class<? extends User> clazz2 = user.getClass();

        // 세번째 방법
        Class<?> clazz3 = Class.forName("org.example.model.User");

        logger.debug("class: [{}]", clazz);
        logger.debug("class: [{}]", clazz2);
        logger.debug("class: [{}]", clazz3);

        Assertions.assertEquals(clazz, clazz2);
        Assertions.assertEquals(clazz2, clazz3);

    }

    private static Set<Class<?>> getTypesAnnotatedWith(List<Class<? extends Annotation>> annotations) {
        //org.example 이라는 해당 패키지 밑에 있는 Controller라는 애노테이션이 붙어져 있는 대상들을 다 찾아서 해당 hashSet에 담는코드 이다.
        Reflections reflections = new Reflections("org.example");

        Set<Class<?>> beans = new HashSet<>();
        annotations.forEach(annotation -> beans.addAll(reflections.getTypesAnnotatedWith(annotation)));
        return beans;
    }
}
