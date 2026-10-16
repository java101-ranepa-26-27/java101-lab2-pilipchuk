package ru.ranepa;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Тесты практики №2. Этот же файл лежит у студентов в src/test/java/ru/ranepa/.
 *
 * Проверяют поведение, а не имена: тест запускает {@code Main} с заданным вводом и
 * смотрит на то, что программа напечатала. Методы, поля и формат отчёта свободны —
 * важно, чтобы в отчёте читались имя, оценки, средний балл и статус.
 *
 * Из имён зафиксированы только два класса: {@code ru.ranepa.Main} (точка входа)
 * и {@code ru.ranepa.Student}.
 */
@Timeout(10)
class StudentTest {

    // --- класс Student ---

    @Test
    void классStudentСуществует() {
        try {
            Class.forName("ru.ranepa.Student");
        } catch (ClassNotFoundException e) {
            fail("нужен класс ru.ranepa.Student — в нём живёт студент из ТЗ");
        }
    }

    // --- что видно в отчёте ---

    @Test
    void вОтчётеВидноИмя() throws Exception {
        String printed = run("Иванов Иван\n5\n4\n5\n0\n");
        assertTrue(printed.contains("Иванов Иван"), printed);
    }

    @Test
    void вОтчётеВидныОценки() throws Exception {
        // средний по 5 4 2 — это 3.67, поэтому проверяем 4 и 2: они могут взяться
        // только из списка оценок, а не из напечатанного среднего
        String printed = run("Петров Пётр\n5\n4\n2\n0\n");
        assertTrue(printed.contains("4"), printed);
        assertTrue(printed.contains("2"), printed);
    }

    // --- средний балл ---

    @Test
    void среднийДробный_аНеЦелоеЧисло() throws Exception {
        // 14/3 = 4.67; при целочисленном делении в отчёте было бы 4
        assertAverage(run("Сидоров Сидор\n5\n4\n5\n0\n"), 14.0 / 3);
    }

    @Test
    void среднийПоМногимОценкам() throws Exception {
        assertAverage(run("Сидоров Сидор\n5\n5\n4\n4\n3\n0\n"), 4.2);
    }

    @Test
    void безОценок_среднийНоль() throws Exception {
        // деления на ноль быть не должно: 0/0 — это Infinity и NaN, а не средний балл
        assertAverage(run("Без Оценок\n0\n"), 0.0);
    }

    @Test
    void безОценок_двоечникИНеInfinity() throws Exception {
        String printed = run("Без Оценок\n0\n");
        assertTrue(printed.contains("Без Оценок"), printed);
        assertStatus(printed, "двоечник");
        assertFalse(printed.contains("Infinity"), printed);
        assertFalse(printed.contains("NaN"), printed);
    }

    // --- статус: пороги включительные ---

    @Test
    void ровноЧетыреСТочекПять_отличник() throws Exception {
        assertStatus(run("Петров Пётр\n4\n5\n0\n"), "отличник");
    }

    @Test
    void ровноТриСТочекПять_хорошист() throws Exception {
        // 7/2 = 3.5; при целочисленном делении 3 → «троечник»
        assertStatus(run("Петров Пётр\n3\n4\n0\n"), "хорошист");
    }

    @Test
    void ровноДваСТочекПять_троечник() throws Exception {
        assertStatus(run("Петров Пётр\n2\n3\n0\n"), "троечник");
    }

    @Test
    void среднийРовный_хорошист() throws Exception {
        // такой случай пройдёт и при целочисленном делении — поэтому нужны ещё и дробные
        assertStatus(run("Петров Пётр\n4\n4\n4\n0\n"), "хорошист");
    }

    // --- ноль как сигнал конца ввода ---

    @Test
    void нольЗавершаетВводИНеСтановитсяОценкой() throws Exception {
        // если ноль попадёт в оценки, средний станет 14/4 = 3.5 и статус съедет на «хорошист»
        assertStatus(run("Иванов Иван\n5\n4\n5\n0\n"), "отличник");
    }

    // --- запуск программы ---

    /** Запускает Main с заданным вводом и возвращает всё, что программа напечатала. */
    private static String run(String input) throws Exception {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            Main.main(new String[0]);
            System.out.flush();
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
        return buffer.toString(StandardCharsets.UTF_8);
    }

    private static final Pattern NUMBER = Pattern.compile("\\d+(?:[.,]\\d+)?");

    /** Есть ли в отчёте число, равное среднему баллу: округлять можно до одного знака после запятой. */
    private static void assertAverage(String printed, double expected) {
        Matcher numbers = NUMBER.matcher(printed);
        while (numbers.find()) {
            double value = Double.parseDouble(numbers.group().replace(',', '.'));
            if (Math.abs(value - expected) <= 0.051) {
                return;
            }
        }
        fail("в отчёте нет среднего балла " + expected
                + " (можно округлить до одного знака после запятой), напечатано:\n" + printed);
    }

    /** Статус ищем без учёта регистра: «Отличник» и «отличник» — одно и то же. */
    private static void assertStatus(String printed, String expected) {
        assertTrue(printed.toLowerCase(Locale.ROOT).contains(expected),
                "в отчёте нет статуса «" + expected + "», напечатано:\n" + printed);
    }
}
