import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
class MyClass {

    @BeforeAll
    static void initAll() {
        System.out.println("Начали");
    }

    @AfterAll
    static void endAll() {
        System.out.println("Закончили");
    }

    @Test
    void test1() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("Test1 " + Thread.currentThread().getName());
    }

    @Test
    void test2() throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("Test 2! " + Thread.currentThread().getName());
    }
}

