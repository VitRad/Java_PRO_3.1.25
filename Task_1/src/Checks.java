import annotations.AfterSuite;
import annotations.BeforeSuite;
import annotations.Test;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Checks {
    private static void checkStaticMethodForBeforeSuite(Class c) {
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class)
                    && !java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                //Целевое решение c блокировкой запуска
//                throw new IllegalStateException(method.getName() + ": Аннотация @BeforeSuite применима только к static методам");
                //Решение для тестирования с неблокирующим сообщением
                System.out.println(method.getName() + ": Аннотация @BeforeSuite применима только к static методам");
            }
        }
    }

    private static void checkStaticMethodForAfterSuite(Class c) {
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AfterSuite.class)
                    && !java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                //Целевое решение c блокировкой запуска
//                throw new IllegalStateException(method.getName() + ": Аннотация @AfterSuite применима только к static методам");
                //Решение для тестирования с неблокирующим сообщением
                System.out.println(method.getName() + ": Аннотация @AfterSuite применима только к static методам");
            }
        }
    }

    private static void checkStaticMethods(Class c) {
        checkStaticMethodForBeforeSuite(c);
        checkStaticMethodForAfterSuite(c);
    }

    private static void checkPriority(Class c) {
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                int priority = method.getAnnotation((Test.class)).priority();
                if (priority < 1 || priority > 10) {
                    //Целевое решение c блокировкой запуска
//                    throw new IllegalArgumentException("Для метода " + method.getName() +
//                            " использован priority=" + priority + ", вне допустимого диапазона от 1 до 10");
                    //Решение для тестирования с неблокирующим сообщением
                    System.out.println("Для метода " + method.getName() +
                            " использован priority=" + priority + ", вне допустимого диапазона от 1 до 10");
                }
            }
        }
    }

    private static void checkCntBeforeSuite(Class c){
        List<Method> beforeSuiteList = new ArrayList<>();
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                beforeSuiteList.add(method);
            }
        }
        if(beforeSuiteList.size()>1){
            ////Целевое решение c блокировкой запуска
//            throw new RuntimeException("Не допустимо использование более одного метода с аннотацией @BeforeSuite");
            System.out.println("Не допустимо использование более одного метода с аннотацией @BeforeSuite");
        }
    }
    private static void checkCntAfterSuite(Class c){
        List<Method> beforeSuiteList = new ArrayList<>();
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AfterSuite.class)) {
                beforeSuiteList.add(method);
            }
        }
        if(beforeSuiteList.size()>1){
            ////Целевое решение c блокировкой запуска
//            throw new RuntimeException("Не допустимо использование более одного метода с аннотацией @AfterSuite");
            System.out.println("Не допустимо использование более одного метода с аннотацией @AfterSuite");
        }
    }
    private static void checkCntBeforeAndAfterSuite(Class c){
        checkCntBeforeSuite(c);
        checkCntAfterSuite(c);
    }

    public static void checks(Class c){
        //Проверка на static для методов
        checkStaticMethods(c);

        //Проверка, что кол-во BeforeSuite и AfterSuite не больше одного
        checkCntBeforeAndAfterSuite(c);

        //Проверка приоритета для тестовых методов
        checkPriority(c);
    }
}
