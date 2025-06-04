import annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Checks {
    public static void checks(Class c) {
        List<Method> beforeSuiteList = new ArrayList<>();
        List<Method> afterSuiteList = new ArrayList<>();
        List<Method> beforeTestList = new ArrayList<>();
        List<Method> afterTestList = new ArrayList<>();
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                beforeSuiteList.add(method);
                if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    throw new IllegalStateException(method.getName() + ": Аннотация @BeforeSuite применима только к static методам");
                }
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                afterSuiteList.add(method);
                if (!java.lang.reflect.Modifier.isStatic(method.getModifiers())) {
                    throw new IllegalStateException(method.getName() + ": Аннотация @AfterSuite применима только к static методам");
                }
            }
            if (method.isAnnotationPresent(BeforeTest.class)) {
                beforeTestList.add(method);

            }
            if (method.isAnnotationPresent(AfterTest.class)) {
                afterTestList.add(method);
            }
            if (method.isAnnotationPresent(Test.class)) {
                int priority = method.getAnnotation((Test.class)).priority();
                if (priority < 1 || priority > 10) {
                    throw new IllegalArgumentException("Для метода " + method.getName() +
                            " установлен priority=" + priority + ", вне допустимого диапазона от 1 до 10");
                }
            }
        }
        if (beforeSuiteList.size() > 1) {
            throw new RuntimeException("Не допустимо использование более одного метода с аннотацией @BeforeSuite");
        }
        if (afterSuiteList.size() > 1) {
            throw new RuntimeException("Не допустимо использование более одного метода с аннотацией @AfterSuite");
        }
        if (beforeTestList.size() > 1) {
            throw new RuntimeException("Не допустимо использование более одного метода с аннотацией @BeforeTest");
        }
        if (afterTestList.size() > 1) {
            throw new RuntimeException("Не допустимо использование более одного метода с аннотацией @AfterTest");
        }
    }
}
