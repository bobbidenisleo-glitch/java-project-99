package hexlet.code.app;

import hexlet.code.AppApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppApplicationTest {

    @Test
    void contextLoads() {
        // Проверяем, что контекст загружается
    }

    @Test
    void testMainMethod() {
        // Вызываем main с пустыми аргументами
        String[] args = {};
        AppApplication.main(args);
        // Если main отработал без исключений - тест пройден
    }
}
