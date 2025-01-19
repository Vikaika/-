package com.example.proj;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.util.EmptyStackException;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;

class CalculatorTest {
    private final Calculator calculator = new Calculator();

    // Тесты для метода ValidExpression
    @Test
    void testValidExpressionWithValidInput() throws Exception {
        String expression = "3 + 4 * (2 - 1)";
        Method validExpressionMethod = Calculator.class.getDeclaredMethod("ValidExpression", String.class);
        validExpressionMethod.setAccessible(true);
        boolean result = (boolean) validExpressionMethod.invoke(calculator, expression);
        assertTrue(result);
    }

    @Test
    void testValidExpressionWithInvalidInput() throws Exception {
        String expression = "3 + + 4";
        Method validExpressionMethod = Calculator.class.getDeclaredMethod("ValidExpression", String.class);
        validExpressionMethod.setAccessible(true);
        boolean result = (boolean) validExpressionMethod.invoke(calculator, expression);
        assertFalse(result);
    }

    // Тесты для метода PlacingBrackets
    @Test
    void testPlacingBracketsWithBalancedBrackets() throws Exception {
        String expression = "(3 + 4) * (2 - 1)";
        Method placingBracketsMethod = Calculator.class.getDeclaredMethod("PlacingBrackets", String.class);
        placingBracketsMethod.setAccessible(true);
        boolean result = (boolean) placingBracketsMethod.invoke(calculator, expression);
        assertTrue(result);
    }

    @Test
    void testPlacingBracketsWithUnbalancedBrackets() throws Exception {
        String expression = "(3 + 4 * (2 - 1)";
        Method placingBracketsMethod = Calculator.class.getDeclaredMethod("PlacingBrackets", String.class);
        placingBracketsMethod.setAccessible(true);
        boolean result = (boolean) placingBracketsMethod.invoke(calculator, expression);
        assertFalse(result);
    }

    // Тесты для метода OperationPriority
    @Test
    void testOperationPriority() throws Exception {
        Method operationPriorityMethod = Calculator.class.getDeclaredMethod("OperationPriority", String.class);
        operationPriorityMethod.setAccessible(true);
        assertEquals(1, operationPriorityMethod.invoke(calculator, "("));
        assertEquals(1, operationPriorityMethod.invoke(calculator, ")"));
        assertEquals(2, operationPriorityMethod.invoke(calculator, "+"));
        assertEquals(2, operationPriorityMethod.invoke(calculator, "-"));
        assertEquals(3, operationPriorityMethod.invoke(calculator, "*"));
        assertEquals(3, operationPriorityMethod.invoke(calculator, "/"));
        assertEquals(4, operationPriorityMethod.invoke(calculator, "^"));
        assertEquals(-1, operationPriorityMethod.invoke(calculator, "%"));
    }

    // Тесты для метода PerformOperation
    @Test
    void testPerformOperationAddition() throws Exception {
        Method performOperationMethod = Calculator.class.getDeclaredMethod("PerformOperation", double.class, double.class, String.class);
        performOperationMethod.setAccessible(true);
        double result = (double) performOperationMethod.invoke(calculator, 3.0, 4.0, "+");
        assertEquals(7.0, result);
    }

    @Test
    void testPerformOperationSubtraction() throws Exception {
        Method performOperationMethod = Calculator.class.getDeclaredMethod("PerformOperation", double.class, double.class, String.class);
        performOperationMethod.setAccessible(true);
        double result = (double) performOperationMethod.invoke(calculator, 10.0, 4.0, "-");
        assertEquals(6.0, result);
    }

    @Test
    void testPerformOperationMultiplication() throws Exception {
        Method performOperationMethod = Calculator.class.getDeclaredMethod("PerformOperation", double.class, double.class, String.class);
        performOperationMethod.setAccessible(true);
        double result = (double) performOperationMethod.invoke(calculator, 3.0, 4.0, "*");
        assertEquals(12.0, result);
    }

    @Test
    void testPerformOperationDivision() throws Exception {
        Method performOperationMethod = Calculator.class.getDeclaredMethod("PerformOperation", double.class, double.class, String.class);
        performOperationMethod.setAccessible(true);
        double result = (double) performOperationMethod.invoke(calculator, 8.0, 4.0, "/");
        assertEquals(2.0, result);
    }

    @Test
    void testPerformOperationDivisionByZero() throws Exception {
        Method performOperationMethod = Calculator.class.getDeclaredMethod("PerformOperation", double.class, double.class, String.class);
        performOperationMethod.setAccessible(true);

        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            performOperationMethod.invoke(calculator, 8.0, 0.0, "/");
        });

        // Извлекаем IllegalArgumentException из InvocationTargetException
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof IllegalArgumentException);
        assertEquals("Cannot divide by zero", cause.getMessage());
    }

    @Test
    void testPerformOperationExponentiation() throws Exception {
        Method performOperationMethod = Calculator.class.getDeclaredMethod("PerformOperation", double.class, double.class, String.class);
        performOperationMethod.setAccessible(true);
        double result = (double) performOperationMethod.invoke(calculator, 2.0, 3.0, "^");
        assertEquals(8.0, result);
    }

    @Test
    void testPerformOperationUnknownOperation() throws Exception {
        Method performOperationMethod = Calculator.class.getDeclaredMethod("PerformOperation", double.class, double.class, String.class);
        performOperationMethod.setAccessible(true);

        Exception exception = assertThrows(InvocationTargetException.class, () -> {
            performOperationMethod.invoke(calculator, 2.0, 3.0, "%");
        });

        // Извлекаем IllegalArgumentException из InvocationTargetException
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof IllegalArgumentException);
        assertEquals("Unknown operation: %", cause.getMessage());
    }

    // Тесты для метода Result
    @Test
    void testResultWithMultipleOperations() throws Exception {
        calculator.getValues().push(3.0);
        calculator.getValues().push(4.0);
        calculator.getOperators().push("+");
        calculator.getValues().push(2.0);
        calculator.getOperators().push("*");
        Method resultMethod = Calculator.class.getDeclaredMethod("Result");
        resultMethod.setAccessible(true);
        String result = (String) resultMethod.invoke(calculator);
        assertEquals("11,00", result); // Ожидаем 11,00
    }

    @Test
    void testResultWithDivision() throws Exception {
        calculator.getValues().push(8.0);
        calculator.getValues().push(4.0);
        calculator.getOperators().push("/");

        Method resultMethod = Calculator.class.getDeclaredMethod("Result");
        resultMethod.setAccessible(true);

        String result = (String) resultMethod.invoke(calculator);
        assertEquals("2,00", result); // Ожидаем 2,00 вместо 2.00
    }

    @Test
    void testResultWithExponentiation() throws Exception {
        calculator.getValues().push(2.0);
        calculator.getValues().push(3.0);
        calculator.getOperators().push("^");
        Method resultMethod = Calculator.class.getDeclaredMethod("Result");
        resultMethod.setAccessible(true);
        String result = (String) resultMethod.invoke(calculator);
        assertEquals("8,00", result); // Ожидаем 8,00
    }



    @Test
    void testResultWithEmptyStacks() throws Exception {
        // Убедитесь, что стеки пустые
        assertTrue(calculator.getValues().isEmpty());
        assertTrue(calculator.getOperators().isEmpty());

        Method resultMethod = Calculator.class.getDeclaredMethod("Result");
        resultMethod.setAccessible(true);

        // Ожидаем, что будет выброшено InvocationTargetException
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> {
            resultMethod.invoke(calculator);
        });

        // Извлекаем истинное исключение
        Throwable cause = exception.getCause();
        assertTrue(cause instanceof EmptyStackException, "Expected an EmptyStackException");
    }

    // Тестирование некорректного выражения с незакрытыми скобками
    @Test
    void testInvalidExpressionWithUnmatchedBrackets() {
        String expression = "(3 + 4 * (2 - 1)";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.Calculate(expression);
        });
        assertEquals("Некорректное выражение", exception.getMessage());
    }

    @Test
    void testCalculateWithDivisionByZero() {
        String expression = "8 / 0";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.Calculate(expression);
        });
        assertEquals("Cannot divide by zero", exception.getMessage());
    }

    // Тестирование выражения с дробными числами
    @Test
    void testCalculateWithDecimals() {
        String expression = "1.5 + 2.5";
        String result = calculator.Calculate(expression);
        assertEquals("4,00", result); // Ожидаем 4,00
    }

    // Тестирование некорректного выражения с недопустимыми символами
    @Test
    void testInvalidExpressionWithInvalidCharacters() {
        String expression = "3 + 4 * a";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.Calculate(expression);
        });
        assertEquals("Некорректное выражение", exception.getMessage());
    }

    // Тестирование выражения с отрицательными числами
    @Test
    void testCalculateWithNegativeNumbers() {
        String expression = "-3 + 4";
        String result = calculator.Calculate(expression);
        assertEquals("1,00", result); // Ожидаем 1,00
    }

    // Тестирование выражения с дробными числами и отрицательными числами
    @Test
    void testCalculateWithNegativeDecimals() {
        String expression = "-1.5 + 2.5";
        String result = calculator.Calculate(expression);
        assertEquals("1,00", result); // Ожидаем 1,00
    }

}
