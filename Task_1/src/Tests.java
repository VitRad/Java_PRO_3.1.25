import annotations.*;

public class Tests {

    @BeforeSuite
    public static void staticTestBeforeSuite() {
        System.out.println("run @BeforeSuite");
    }

    @AfterSuite
    public static void staticTestAfterSuite() {
        System.out.println("run @AfterSuite");
    }

    @BeforeTest
    public void testBeforeTest() {
        System.out.println("run @BeforeTest");
    }

    @AfterTest
    public void testAfterTest() {
        System.out.println("run @AfterTest");
    }

    @Test
    public void test5() {
        System.out.println("run @Test(priority=5)");
    }

    @Test(priority = 1)
    public void test1() {
        System.out.println("run @Test(priority = 1)");
    }

    @Test(priority = 8)
    public void test8() {
        System.out.println("run @Test(priority = 8)");
    }

    @Test(priority = 2)
    @CsvSource("10, Java, 20, true")
    public void test2CsvSource(int firstParam, String secondParam, int thirdParam, boolean fourthParam) {
        System.out.println("run @Test(priority = 2) with @CsvSource(\"10, Java, 20, true\")");
        System.out.println("[firstParam:" + firstParam +
                ", secondParam:" + secondParam +
                ", thirdParam:" + thirdParam +
                ", boolean:" + fourthParam + "]");
    }
}
