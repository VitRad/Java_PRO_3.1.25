import annotations.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;

public class TestRunner {

    public static void runTests(Class c) {
        try {
            //Создание объекта тестируемого класса
            Object testClass = c.getDeclaredConstructor().newInstance();

            //Проверки
            Checks.checks(c);

            //Выполнение @BeforeSuite
            beforeSuiteRunner(c, testClass);

            //Выполнение @Test
            testRunner(c, testClass);

            //Выполнение @AfterSuite
            afterSuiteRunner(c, testClass);
        } catch (Exception e) {
            //Ловим все исключения, для которых не предусмотрели индивидуальную обработку
            System.out.println("Ошибка при запуске тестов:" + e);
        }
    }

    private static void beforeSuiteRunner(Class c, Object testMethod) throws InvocationTargetException, IllegalAccessException {
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                method.invoke(testMethod);
            }
        }
    }

    private static void afterSuiteRunner(Class c, Object testMethod) throws InvocationTargetException, IllegalAccessException {
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AfterSuite.class)) {
                method.invoke(testMethod);
            }
        }
    }

    private static void testRunner(Class c, Object testMethod) throws InvocationTargetException, IllegalAccessException {
        runTest(c, testMethod);
    }

    private static void runBeforeTest(Class c, Object testMethod) throws InvocationTargetException, IllegalAccessException {
        for (Method method : c.getDeclaredMethods()) {
            method.getAnnotation(c);
            if (method.isAnnotationPresent(BeforeTest.class)) {
                method.invoke(testMethod);
            }
        }
    }

    private static void runCsvSourceTest(Method testMethod, Object test) throws InvocationTargetException, IllegalAccessException {
        CsvSource csvSource = testMethod.getAnnotation(CsvSource.class);
        String sourceValue = csvSource.value();
        if (sourceValue != null) {
            String[] values = sourceValue.replace(" ", "").split(",");
            Object[] args = new Object[values.length];
            if (values.length == 4) {
                for (int i = 0; i < values.length; i++) {
                    if (i == 0) {
                        int fistParam = Integer.parseInt(values[i]);
                        args[i] = fistParam;
                    }
                    if (i == 1) {
                        String secondParam = values[i];
                        args[i] = secondParam;
                    }
                    if (i == 2) {
                        int thirdParam = Integer.parseInt(values[i]);
                        args[i] = thirdParam;
                    }
                    if (i == 3) {
                        boolean fourthParam = Boolean.parseBoolean(values[i]);
                        args[i] = fourthParam;
                    }
                }
            }
            testMethod.invoke(test, args);
        }
    }

    private static void runTest(Class c, Object testMethod) throws InvocationTargetException, IllegalAccessException {
        Map<Integer, Method> methodMap = new TreeMap<>();
        //Получение списка всех тестов, аннотированных @Test
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Test.class)) {
                int priority = method.getAnnotation((Test.class)).priority();
                methodMap.put(priority, method);
            }
        }
        //Запуск тестов
        for (Map.Entry<Integer, Method> map : methodMap.entrySet()) {
            runBeforeTest(c, testMethod);
            if(map.getValue().isAnnotationPresent(CsvSource.class)){
                runCsvSourceTest(map.getValue(), testMethod);
            }else {
            map.getValue().invoke(testMethod);
            }
            runAfterTest(c, testMethod);
        }
    }

    private static void runAfterTest(Class c, Object testMethod) throws InvocationTargetException, IllegalAccessException {
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(AfterTest.class)) {
                method.invoke(testMethod);
            }
        }
    }
}
