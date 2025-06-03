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
            //Запуск
            prepereAndRun(c, testClass);
        } catch (Exception e) {
            //Ловим все исключения, для которых не предусмотрели индивидуальную обработку
            System.out.println("Ошибка при запуске тестов:" + e);
        }
    }

    private static void prepereAndRun(Class c, Object testMethod) throws InvocationTargetException, IllegalAccessException {
        Method beforeSuite = null;
        Method afterSuite = null;
        Method beforeTest = null;
        Method afterTest = null;
        Map<Integer, Method> testMap = new TreeMap<>();
        for (Method method : c.getDeclaredMethods()) {
            if (method.isAnnotationPresent(BeforeSuite.class)) {
                beforeSuite = method;
            }
            if (method.isAnnotationPresent(AfterSuite.class)) {
                afterSuite = method;
            }
            if (method.isAnnotationPresent(BeforeTest.class)) {
                beforeTest = method;
            }
            if (method.isAnnotationPresent(AfterTest.class)) {
                afterTest = method;
            }
            if (method.isAnnotationPresent(Test.class)) {
                int priority = method.getAnnotation((Test.class)).priority();
                testMap.put(priority, method);
            }
        }
        //Запуск @BeforeSuite
        runMethod(beforeSuite, testMethod);
        //Запуск @Test
        for (Map.Entry<Integer, Method> map : testMap.entrySet()) {
            runMethod(beforeTest, testMethod);
            if (map.getValue().isAnnotationPresent(CsvSource.class)) {
                runCsvSourceTest(map.getValue(), testMethod);
            } else {
                runMethod(map.getValue(), testMethod);
            }
            runMethod(afterTest, testMethod);
        }
        //Запуск @AfterSuite
        runMethod(afterSuite, testMethod);
    }

    private static void runMethod(Method method, Object testMethod) throws InvocationTargetException, IllegalAccessException {
        if (method != null || testMethod != null) {
            method.invoke(testMethod);
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
}
